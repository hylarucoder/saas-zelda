package xyz.staffjoy.common.auth

import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizeInterceptor : HandlerInterceptorAdapter() {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val authorize = handler.method.getAnnotation(Authorize::class.java)
                ?: return true // no need to authorize
        val allowedHeaders: Array<String> = authorize.value as Array<String>
        val authzHeader = request.getHeader(AuthConstant.AUTHORIZATION_HEADER)
        if (StringUtils.isEmpty(authzHeader)) {
            throw PermissionDeniedException(AuthConstant.ERROR_MSG_MISSING_AUTH_HEADER)
        }
        if (!Arrays.asList(*allowedHeaders).contains(authzHeader)) {
            throw PermissionDeniedException(AuthConstant.ERROR_MSG_DO_NOT_HAVE_ACCESS)
        }
        return true
    }
}