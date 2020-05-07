package xyz.staffjoy.faraday.core.filter

import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FaviconFilter(private val faviconFile: ByteArray) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (FAVICON_PATH == request.requestURI) {
            response.status = HttpServletResponse.SC_OK
            response.outputStream.write(faviconFile)
        } else {
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        const val FAVICON_PATH = "/favicon.ico"
    }

}