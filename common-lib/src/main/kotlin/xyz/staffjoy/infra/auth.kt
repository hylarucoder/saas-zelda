package xyz.staffjoy.common
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import xyz.staffjoy.common.AuthContext.userId

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import xyz.staffjoy.common.crypto.Sign
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.Cookie

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
annotation class Authorize( // allowed consumers
        vararg val value: String)

object AuthConstant {
    const val COOKIE_NAME = "staffjoy-faraday"

    // header set for internal user id
    const val CURRENT_USER_HEADER = "faraday-current-user-id"

    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    const val AUTHORIZATION_HEADER = "Authorization"

    // AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
    // a request is being made bu an unauthenticated web user
    const val AUTHORIZATION_ANONYMOUS_WEB = "faraday-anonymous"

    // AUTHORIZATION_COMPANY_SERVICE is set as the Authorization header to denote
    // that a request is being made by the company service
    const val AUTHORIZATION_COMPANY_SERVICE = "company-service"

    // AUTHORIZATION_BOT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the bot microservice
    const val AUTHORIZATION_BOT_SERVICE = "bot-service"

    // AUTHORIZATION_ACCOUNT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the account service
    const val AUTHORIZATION_ACCOUNT_SERVICE = "account-service"

    // AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
    // a request is being made by a Staffjoy team member
    const val AUTHORIZATION_SUPPORT_USER = "faraday-support"

    // AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
    // denote that a request is being made by the dev-only superpowers service
    const val AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service"

    // AUTHORIZATION_WWW_SERVICE is set as the Authorization header to denote that
    // a request is being made by the www login / signup system
    const val AUTHORIZATION_WWW_SERVICE = "www-service"

    // AUTH_WHOAMI_SERVICE is set as the Authorization heade to denote that
    // a request is being made by the whoami microservice
    const val AUTHORIZATION_WHOAMI_SERVICE = "whoami-service"

    // AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
    // a request is being made by an authenticated we6b user
    const val AUTHORIZATION_AUTHENTICATED_USER = "faraday-authenticated"

    // AUTHORIZATION_ICAL_SERVICE is set as the Authorization header to denote that
    // a request is being made by the ical service
    const val AUTHORIZATION_ICAL_SERVICE = "ical-service"

    // AUTH ERROR Messages
    const val ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service"
    const val ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header"
}


object AuthContext {
    private fun getRequetHeader(headerName: String): String? {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes is ServletRequestAttributes) {
            val request = requestAttributes.request
            return request.getHeader(headerName)
        }
        return null
    }

    @JvmStatic
    val userId: String?
        get() = getRequetHeader(AuthConstant.CURRENT_USER_HEADER)

    val authz: String?
        get() = getRequetHeader(AuthConstant.AUTHORIZATION_HEADER)
}

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



object Sessions {
    private val SHORT_SESSION = TimeUnit.HOURS.toMillis(12)
    private val LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24.toLong())
    fun loginUser(userId: String?,
                  support: Boolean,
                  rememberMe: Boolean,
                  signingSecret: String?,
                  externalApex: String?,
                  response: HttpServletResponse) {
        val maxAge: Int
        val duration: Long = if (rememberMe) {
            // "Remember me"
            LONG_SESSION
        } else {
            SHORT_SESSION
        }
        maxAge = (duration / 1000).toInt()
        val token = signingSecret?.let { Sign.generateSessionToken(userId, it, support, duration) }
        val cookie = Cookie(AuthConstant.COOKIE_NAME, token)
        cookie.path = "/"
        cookie.domain = externalApex
        cookie.maxAge = maxAge
        cookie.isHttpOnly = true
        response.addCookie(cookie)
    }

    fun getToken(request: HttpServletRequest): String? {
        val cookies = request.cookies
        if (cookies == null || cookies.isEmpty()) return null
        val tokenCookie = Arrays.stream(cookies)
                .filter { cookie: Cookie -> AuthConstant.COOKIE_NAME == cookie.name }
                .findAny().orElse(null)
                ?: return null
        return tokenCookie.value
    }

    fun logout(externalApex: String?, response: HttpServletResponse) {
        val cookie = Cookie(AuthConstant.COOKIE_NAME, "")
        cookie.path = "/"
        cookie.maxAge = 0
        cookie.domain = externalApex
        response.addCookie(cookie)
    }
}

class FeignRequestHeaderInterceptor : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        val userId = userId
        if (!StringUtils.isEmpty(userId)) {
            requestTemplate.header(AuthConstant.CURRENT_USER_HEADER, userId)
        }
    }
}