package xyz.zelda.web.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.account.dto.VerifyPasswordRequest
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Sessions
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.services.Service
import xyz.zelda.infra.services.ServiceDirectory
import xyz.zelda.web.props.AppProps
import xyz.zelda.web.service.HelperService
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.LoginPage
import xyz.zelda.web.view.PageFactory
import java.net.MalformedURLException
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class LoginController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @Autowired
    private val envConfig: EnvConfig? = null

    @Autowired
    private val appProps: AppProps? = null

    @Autowired
    private val helperService: HelperService? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @RequestMapping(value = "/login")
    fun login(@RequestParam(value = "return_to", required = false) returnTo: String,  // POST and GET are in the same handler - reset
              @RequestParam(value = "email", required = false) email: String?,
              @RequestParam(value = "password", required = false) password: String?,  // rememberMe=True means that the session is set for a month instead of a day
              @RequestParam(value = "remember-me", required = false) rememberMe: String?,
              model: Model,
              request: HttpServletRequest?,
              response: HttpServletResponse?): String {
        var returnTo = returnTo
        val loginPage: LoginPage = pageFactory.buildLoginPage()
        loginPage.setReturnTo(returnTo) // for GET

        // if logged in - go away
        if (!StringUtils.isEmpty(AuthContext.getAuthz()) && !AuthConstant.AUTHORIZATION_ANONYMOUS_WEB.equals(AuthContext.getAuthz())) {
            val url: String = HelperService.buildUrl("http", "myaccount." + envConfig.getExternalApex())
            return "redirect:$url"
        }
        if (HelperService.isPost(request)) {
            var account: AccountDto? = null
            var genericAccountResponse: GenericAccountResponse? = null
            try {
                val verifyPasswordRequest: VerifyPasswordRequest = VerifyPasswordRequest.builder()
                        .email(email)
                        .password(password)
                        .build()
                genericAccountResponse = accountClient.verifyPassword(AuthConstant.AUTHORIZATION_WWW_SERVICE, verifyPasswordRequest)
            } catch (ex: Exception) {
                helperService.logException(logger, ex, "fail to verify user password")
            }
            if (genericAccountResponse != null) {
                if (!genericAccountResponse.isSuccess()) {
                    helperService.logError(logger, genericAccountResponse.getMessage())
                } else {
                    account = genericAccountResponse.getAccount()
                }
            }
            if (account != null) { // login success
                // set cookie
                Sessions.loginUser(account.getId(),
                        account.isSupport(),
                        !StringUtils.isEmpty(rememberMe),
                        appProps.getSigningSecret(),
                        envConfig.getExternalApex(),
                        response)
                helperService.trackEventAsync(account.getId(), "login")
                helperService.syncUserAsync(account.getId())
                var scheme = "https"
                if (envConfig.isDebug()) {
                    scheme = "http"
                }
                if (StringUtils.isEmpty(returnTo)) {
                    returnTo = HelperService.buildUrl(scheme, "app." + envConfig.getExternalApex())
                } else {
                    if (!returnTo.startsWith("http")) {
                        returnTo = "http://$returnTo"
                    }
                    // sanitize
                    if (!isValidSub(returnTo)) {
                        returnTo = HelperService.buildUrl(scheme, "myaccount." + envConfig.getExternalApex())
                    }
                }
                return "redirect:$returnTo"
            } else {
                logger.info("Login attempt denied", "email", email)
                loginPage.setDenied(true)
                loginPage.setPreviousEmail(email)
            }
        }
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, loginPage)
        return Constant.VIEW_LOGIN
    }

    // isValidSub returns true if url contains valid subdomain
    fun isValidSub(sub: String?): Boolean {
        var url: URL? = null
        url = try {
            URL(sub)
        } catch (ex: MalformedURLException) {
            logger.error("can't parse url", ex)
            return false
        }
        val bare = url.host.replace("." + envConfig.getExternalApex().toRegex(), "")
        val serviceMap: Map<String, Service> = ServiceDirectory.getMapping()
        for (key in serviceMap.keys) {
            if (key == bare) {
                return true
            }
        }
        return false
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(LoginController::class.java)
    }
}