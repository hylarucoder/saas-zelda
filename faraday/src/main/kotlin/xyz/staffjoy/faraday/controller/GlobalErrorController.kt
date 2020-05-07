package xyz.staffjoy.faraday.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class GlobalErrorController : ErrorController {
//    @Autowired
//    var errorPageFactory: ErrorPageFactory? = null

//    @Autowired
//    var sentryClient: SentryClient? = null

//    @Autowired
//    var staffjoyProps: StaffjoyProps? = null
//
//    @Autowired
//    var envConfig: EnvConfig? = null

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String {
//        val statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
//        val exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)
//        var errorPage: ErrorPage? = null
//        if (exception is ForbiddenException) {
//            errorPage = errorPageFactory!!.buildForbiddenErrorPage()
//        } else if (exception is ResourceAccessException) {
//            if (exception.contains(SocketTimeoutException::class.java)) {
//                errorPage = errorPageFactory!!.buildTimeoutErrorPage()
//            }
//        }
//        if (errorPage == null) {
//            errorPage = errorPageFactory!!.buildInternalServerErrorPage()
//        }
//        if (exception != null) {
////            if (envConfig.isDebug()) {  // no sentry in debug mode
////                logger.error("Global error handling", exception)
////            } else {
////                sentryClient.sendException(exception as Exception)
////                val uuid: UUID = sentryClient.getContext().getLastEventId()
////                errorPage.setSentryErrorId(uuid.toString())
////                errorPage.setSentryPublicDsn(staffjoyProps.getSentryDsn())
////                logger.warn("Reported error to sentry", "id", uuid.toString(), "error", exception)
////            }
//        }
//        model.addAttribute("page", errorPage)
        return "error"
    }

    override fun getErrorPath(): String {
        return "/error"
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(GlobalErrorController::class.java)
    }
}