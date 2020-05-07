package xyz.staffjoy.faraday.auth

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.util.StringUtils
import xyz.staffjoy.faraday.auth.AuthContext.userId

/**
 * Feign interceptor，for passing auth info to backend
 *
 * @author bobo
 */
class FeignRequestHeaderInterceptor : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        val userId = userId
        if (!StringUtils.isEmpty(userId)) {
            requestTemplate.header(AuthConstant.CURRENT_USER_HEADER, userId)
        }
    }
}