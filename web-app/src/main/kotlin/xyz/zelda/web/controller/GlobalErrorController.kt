package xyz.zelda.web.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import xyz.zelda.infra.config.StaffjoyProps
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.error.ErrorPage
import xyz.zelda.web.view.error.ErrorPageFactory
import java.util.*
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class GlobalErrorController : ErrorController {
    @Autowired
    var errorPageFactory: ErrorPageFactory? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Autowired
    var staffjoyProps: StaffjoyProps? = null

    @Autowired
    var envConfig: EnvConfig? = null
    val errorPath: String
        get() = "/error"

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String {
        val statusCode: Any = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val exception: Any = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)
        var errorPage: ErrorPage? = null
        errorPage = if (statusCode != null && statusCode as Int === HttpStatus.NOT_FOUND.value()) {
            errorPageFactory.buildNotFoundPage()
        } else {
            errorPageFactory.buildInternalServerErrorPage()
        }
        if (exception != null) {
            if (envConfig.isDebug()) {  // no sentry aop in debug mode
                logger.error("Global error handling", exception)
            } else {
                sentryClient.sendException(exception as Exception)
                val uuid: UUID = sentryClient.getContext().getLastEventId()
                errorPage.setSentryErrorId(uuid.toString())
                errorPage.setSentryPublicDsn(staffjoyProps.getSentryDsn())
                logger.warn("Reported error to sentry", "id", uuid.toString(), "error", exception)
            }
        }
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, errorPage)
        return "error"
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(GlobalErrorController::class.java)
    }
}