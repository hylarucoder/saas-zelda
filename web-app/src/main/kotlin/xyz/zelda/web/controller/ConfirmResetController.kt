package xyz.zelda.web.controller

import com.auth0.jwt.interfaces.DecodedJWT
import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.account.dto.UpdatePasswordRequest
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.Sessions
import xyz.zelda.infra.crypto.Sign
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.error.ServiceException
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.company.dto.AdminOfList
import xyz.zelda.company.dto.GetAdminOfResponse
import xyz.zelda.company.dto.GetWorkerOfResponse
import xyz.zelda.company.dto.WorkerOfList
import xyz.zelda.web.props.AppProps
import xyz.zelda.web.service.HelperService
import xyz.zelda.web.view.ConfirmResetPage
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.PageFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ConfirmResetController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @Autowired
    private val appProps: AppProps? = null

    @Autowired
    private val envConfig: EnvConfig? = null

    @Autowired
    private val helperService: HelperService? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val companyClient: CompanyClient? = null

    @RequestMapping(value = "/reset/{token}")
    fun reset(@PathVariable token: String?,
              @RequestParam(value = "password", required = false) password: String,
              model: Model,
              request: HttpServletRequest?,
              response: HttpServletResponse?): String {
        val page: ConfirmResetPage = pageFactory.buildConfirmResetPage()
        page.setToken(token)
        var email: String? = null
        var userId: String? = null
        try {
            val jwt: DecodedJWT = Sign.verifyEmailConfirmationToken(token, appProps.getSigningSecret())
            email = jwt.getClaim(Sign.CLAIM_EMAIL).asString()
            userId = jwt.getClaim(Sign.CLAIM_USER_ID).asString()
        } catch (ex: Exception) {
            val errMsg = "Failed to verify email confirmation token"
            helperService.logException(logger, ex, errMsg)
            return "redirect:" + ResetController.PASSWORD_RESET_PATH
        }
        if (!HelperService.isPost(request)) {
            model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, page)
            return Constant.VIEW_CONFIRM_RESET
        }

        // isPost
        if (password.length < 6) {
            page.setErrorMessage("Your password must be at least 6 characters long")
            model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, page)
            return Constant.VIEW_CONFIRM_RESET
        }
        var genericAccountResponse1: GenericAccountResponse? = null
        genericAccountResponse1 = try {
            accountClient.getAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "fail to get user account"
            helperService.logException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!genericAccountResponse1.isSuccess()) {
            helperService.logError(logger, genericAccountResponse1.getMessage())
            throw ServiceException(genericAccountResponse1.getMessage())
        }
        val account: AccountDto = genericAccountResponse1.getAccount()
        account.setEmail(email)
        account.setConfirmedAndActive(true)
        var genericAccountResponse2: GenericAccountResponse? = null
        genericAccountResponse2 = try {
            accountClient.updateAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, account)
        } catch (ex: Exception) {
            val errMsg = "fail to update user account"
            helperService.logException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!genericAccountResponse2.isSuccess()) {
            helperService.logError(logger, genericAccountResponse2.getMessage())
            throw ServiceException(genericAccountResponse2.getMessage())
        }

        // Update password
        var baseResponse: BaseResponse? = null
        baseResponse = try {
            val updatePasswordRequest: UpdatePasswordRequest = UpdatePasswordRequest.builder()
                    .userId(userId)
                    .password(password)
                    .build()
            accountClient.updatePassword(AuthConstant.AUTHORIZATION_WWW_SERVICE, updatePasswordRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to update password"
            helperService.logException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!baseResponse.isSuccess()) {
            helperService.logError(logger, baseResponse.getMessage())
            throw ServiceException(baseResponse.getMessage())
        }

        // login user
        Sessions.loginUser(account.getId(),
                account.isSupport(),
                false,
                appProps.getSigningSecret(),
                envConfig.getExternalApex(),
                response)
        logger.info("user activated account and logged in", "user_id", account.getId())

        // Smart redirection - for onboarding purposes
        var workerOfResponse: GetWorkerOfResponse? = null
        workerOfResponse = try {
            companyClient.getWorkerOf(AuthConstant.AUTHORIZATION_WWW_SERVICE, account.getId())
        } catch (ex: Exception) {
            val errMsg = "fail to get worker of list"
            helperService.logException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!workerOfResponse.isSuccess()) {
            helperService.logError(logger, workerOfResponse.getMessage())
            throw ServiceException(workerOfResponse.getMessage())
        }
        val workerOfList: WorkerOfList = workerOfResponse.getWorkerOfList()
        var getAdminOfResponse: GetAdminOfResponse? = null
        getAdminOfResponse = try {
            companyClient.getAdminOf(AuthConstant.AUTHORIZATION_WWW_SERVICE, account.getId())
        } catch (ex: Exception) {
            val errMsg = "fail to get admin of list"
            helperService.logException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!getAdminOfResponse.isSuccess()) {
            helperService.logError(logger, getAdminOfResponse.getMessage())
            throw ServiceException(getAdminOfResponse.getMessage())
        }
        val adminOfList: AdminOfList = getAdminOfResponse.getAdminOfList()
        var destination: String? = null
        destination = if (adminOfList.getCompanies().size() !== 0 || account.isSupport()) {
            HelperService.buildUrl("http", "app." + envConfig.getExternalApex())
        } else if (workerOfList.getTeams().size() !== 0) {
            HelperService.buildUrl("http", "myaccount." + envConfig.getExternalApex())
        } else {
            // onboard
            HelperService.buildUrl("http", "www." + envConfig.getExternalApex(), "/new_company/")
        }
        return "redirect:$destination"
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(ConfirmResetController::class.java)
    }
}