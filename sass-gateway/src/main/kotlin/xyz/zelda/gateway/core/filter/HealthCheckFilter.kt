package xyz.zelda.gateway.core.filter

import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HealthCheckFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (HEALTH_CHECK_PATH == request.requestURI) {
            response.status = HttpServletResponse.SC_OK
            response.writer.println("OK")
        } else {
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        // HEALTH_CHECK_PATH is the standard healthcheck path in our app
        const val HEALTH_CHECK_PATH = "/health"
    }
}