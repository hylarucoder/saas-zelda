package xyz.zelda.gateway.view

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import xyz.zelda.gateway.view.AssetLoader
import xyz.zelda.gateway.view.ErrorPage

@Component
class ErrorPageFactory {
    @Autowired
    var assetLoader: AssetLoader? = null
    fun buildTimeoutErrorPage(): ErrorPage {
        return ErrorPage(
                "Timeout Error",
                "Sorry, our servers seem to be slow. Please try again in a moment.",
                HttpStatus.GATEWAY_TIMEOUT.value(),
                "Click here to check out our system status page",
                "https://status.staffjoy.xyz",
                assetLoader!!.imageBase64
        )
    }

    fun buildForbiddenErrorPage(): ErrorPage {
        return ErrorPage(
                "Access Forbidden",
                "Sorry, it looks like you do not have permission to access this page.",
                HttpStatus.FORBIDDEN.value(),
                "Contact our support team for help",
                "mailto:help@staffjoy.xyz",
                assetLoader!!.imageBase64
                )
    }

    fun buildInternalServerErrorPage(): ErrorPage {
        return ErrorPage(
                "Internal Server Error",
                "Oops! Something broke. We're paging our engineers to look at it immediately.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Click here to check out our system status page",
                "https://status.staffjoy.xyz",
                assetLoader!!.imageBase64
                )
    }
}