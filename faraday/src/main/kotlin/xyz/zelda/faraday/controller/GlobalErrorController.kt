package xyz.zelda.faraday

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
//import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.ResourceAccessException
import xyz.zelda.faraday.exceptions.ForbiddenException
import xyz.zelda.faraday.view.ErrorPage
import xyz.zelda.faraday.view.ErrorPageFactory
import java.net.SocketTimeoutException
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class GlobalErrorController : ErrorController {
    @Autowired
    var errorPageFactory: ErrorPageFactory? = null

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String {
        val statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)
        var errorPage: ErrorPage? = null
        if (exception is ForbiddenException) {
            errorPage = errorPageFactory!!.buildForbiddenErrorPage()
        } else if (exception is ResourceAccessException) {
            if (exception.contains(SocketTimeoutException::class.java)) {
                errorPage = errorPageFactory!!.buildTimeoutErrorPage()
            }
        }
        if (errorPage == null) {
            errorPage = errorPageFactory!!.buildInternalServerErrorPage()
        }
        if (exception != null) {
            logger.error("Global error handling", exception)
//            if (envConfig.isDebug()) {  // no sentry in debug mode
//                logger.error("Global error handling", exception)
//            } else {
//                sentryClient.sendException(exception as Exception)
//                val uuid: UUID = sentryClient.getContext().getLastEventId()
//                errorPage.sentryErrorId = uuid.toString()
//                errorPage.sentryPublicDsn = staffjoyProps.sentryDsn()
//                logger.warn("Reported error to sentry", "id", uuid.toString(), "error", exception)
//            }
        }
        model.addAttribute("page", errorPage)
        return "error"
    }

    override fun getErrorPath(): String {
        return "/error"
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(GlobalErrorController::class.java)
    }
}