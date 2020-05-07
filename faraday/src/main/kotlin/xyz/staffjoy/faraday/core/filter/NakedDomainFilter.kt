package xyz.staffjoy.faraday.core.filter

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.web.filter.OncePerRequestFilter
import xyz.staffjoy.common.env.EnvConfig
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class NakedDomainFilter(private val envConfig: EnvConfig) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        // if you're hitting naked domain - go to www
        // e.g. staffjoy.xyz/foo?true=1 should redirect to www.staffjoy.xyz/foo?true=1
        if (envConfig.getExternalApex().equals(request.serverName)) {
            // It's hitting naked domain - redirect to www
            log.info("hitting naked domain - redirect to www")
            var scheme = "http"
            if (!envConfig.isDebug()) {
                scheme = "https"
            }
            try {
                val redirectUrl = URI(scheme,
                        null,
                        DEFAULT_SERVICE + "." + envConfig.getExternalApex(),
                        request.serverPort,
                        "/login/", null, null)
                response.sendRedirect(redirectUrl.toString())
            } catch (e: URISyntaxException) {
                log.error("fail to build redirect url", e)
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        private val log: ILogger = SLoggerFactory.getLogger(NakedDomainFilter::class.java)
        private const val DEFAULT_SERVICE = "www"
    }

}