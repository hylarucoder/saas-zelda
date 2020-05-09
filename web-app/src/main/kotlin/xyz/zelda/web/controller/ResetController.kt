package xyz.zelda.web.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.PasswordResetRequest
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.error.ServiceException
import xyz.zelda.web.service.HelperService
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.PageFactory
import javax.servlet.http.HttpServletRequest

@Controller
class ResetController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val helperService: HelperService? = null

    @RequestMapping(value = PASSWORD_RESET_PATH)
    fun passwordReset(@RequestParam(value = "email", required = false) email: String?,
                      model: Model,
                      request: HttpServletRequest?): String {

        // TODO google recaptcha handling ignored for training/demo purpose
        // reference : https://www.google.com/recaptcha
        if (HelperService.isPost(request)) {
            val passwordResetRequest: PasswordResetRequest = PasswordResetRequest.builder()
                    .email(email)
                    .build()
            var baseResponse: BaseResponse? = null
            baseResponse = try {
                accountClient.requestPasswordReset(AuthConstant.AUTHORIZATION_WWW_SERVICE, passwordResetRequest)
            } catch (ex: Exception) {
                val errMsg = "Failed password reset"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!baseResponse.isSuccess()) {
                helperService.logError(logger, baseResponse.getMessage())
                throw ServiceException(baseResponse.getMessage())
            }
            logger.info("Initiating password reset")
            model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildResetConfirmPage())
            return Constant.VIEW_CONFIRM
        }
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildResetPage())
        return Constant.VIEW_RESET
    }

    companion object {
        const val PASSWORD_RESET_PATH = "/password-reset"
        val logger: ILogger = SLoggerFactory.getLogger(ResetController::class.java)
    }
}