//package xyz.staffjoy.faraday.core.filter
//
//import com.github.structlog4j.ILogger
//import com.github.structlog4j.SLoggerFactory
//import org.apache.commons.lang3.StringUtils.isEmpty
//import org.springframework.http.HttpHeaders
//import org.springframework.web.filter.OncePerRequestFilter
//import xyz.staffjoy.common.env.EnvConfig
//import java.io.IOException
//import java.net.URI
//import java.net.URISyntaxException
//import javax.servlet.FilterChain
//import javax.servlet.ServletException
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//class SecurityFilter(private val envConfig: EnvConfig) : OncePerRequestFilter() {
//    @Throws(ServletException::class, IOException::class)
//    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
//        // TODO - Determine how to force SSL. Depends on frontend load balancer config.
//        val origin = request.getHeader("Origin")
//        if (!isEmpty(origin)) {
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, PUT, DELETE")
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Accept, Content-Type, Content-Length, Cookie, Accept-Encoding, X-CSRF-Token, Authorization")
//        }
//
//        // Stop here if its Preflighted OPTIONS request
//        if ("OPTIONS" == request.method) {
//            return
//        }
//        if (!envConfig.isDebug()) {
//            // Check if secure
//            var isSecure = request.isSecure
//            if (!isSecure) {
//                // Check if frontend proxy proxied it
//                if ("https" == request.getHeader("X-Forwarded-Proto")) {
//                    isSecure = true
//                }
//            }
//
//            // If not secure, then redirect
//            if (!isSecure) {
//                log.info("Insecure quest in uat&prod environment, redirect to https")
//                try {
//                    val redirectUrl = URI("https",
//                            request.serverName,
//                            request.requestURI, null)
//                    response.sendRedirect(redirectUrl.toString())
//                } catch (e: URISyntaxException) {
//                    log.error("fail to build redirect url", e)
//                }
//                return
//            }
//
//            // HSTS - force SSL
//            response.setHeader("Strict-Transport-Security", "max-age=315360000; includeSubDomains; preload")
//            // No iFrames
//            response.setHeader("X-Frame-Options", "DENY")
//            // Cross-site scripting protection
//            response.setHeader("X-XSS-Protection", "1; mode=block")
//        }
//        filterChain.doFilter(request, response)
//    }
//
//    companion object {
//        private val log: ILogger = SLoggerFactory.getLogger(SecurityFilter::class.java)
//    }
//
//}