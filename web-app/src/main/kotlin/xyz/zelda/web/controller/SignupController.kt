package xyz.zelda.web.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.CreateAccountRequest
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.web.service.HelperService
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.PageFactory
import xyz.zelda.web.controller.LoginController

@Controller
class SignupController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val helperService: HelperService? = null

    @PostMapping(value = "/confirm")
    fun signUp(@RequestParam(value = "name", required = false) name: String?, @RequestParam("email") email: String?, model: Model): String {
        if (!StringUtils.hasText(email)) {
            return SIGN_UP_REDIRECT_PATH
        }
        val request: CreateAccountRequest = CreateAccountRequest.builder()
                .name(name)
                .email(email)
                .build()
        var genericAccountResponse: GenericAccountResponse? = null
        genericAccountResponse = try {
            accountClient.createAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, request)
        } catch (ex: Exception) {
            val errMsg = "Failed to create account"
            helperService.logException(logger, ex, errMsg)
            return SIGN_UP_REDIRECT_PATH
        }
        if (!genericAccountResponse.isSuccess()) {
            helperService.logError(logger, genericAccountResponse.getMessage())
            return SIGN_UP_REDIRECT_PATH
        }
        val account: AccountDto = genericAccountResponse.getAccount()
        logger.info(java.lang.String.format("New Account signup - %s", account))
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildConfirmPage())
        return Constant.VIEW_CONFIRM
    }

    companion object {
        const val SIGN_UP_REDIRECT_PATH = "redirect:/sign-up"
        val logger: ILogger = SLoggerFactory.getLogger(LoginController::class.java)
    }
}