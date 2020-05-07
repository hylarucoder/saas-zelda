package xyz.staffjoy.common.auth

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 *
 * A context holder class for holding the current userId and authz info
 *
 * @author bobo
 */
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