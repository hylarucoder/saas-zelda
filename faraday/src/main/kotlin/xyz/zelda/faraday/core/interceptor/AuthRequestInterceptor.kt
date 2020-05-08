package xyz.zelda.faraday.core.interceptor

import com.auth0.jwt.interfaces.DecodedJWT
import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.Sessions.getToken
import xyz.zelda.infra.crypto.Sign
import xyz.zelda.infra.crypto.Sign.verifySessionToken
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.services.SecurityConstant
import xyz.zelda.infra.services.Service
import xyz.zelda.infra.services.ServiceDirectory
import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.RequestData
import xyz.zelda.faraday.exceptions.FaradayException
import xyz.zelda.faraday.exceptions.ForbiddenException
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.servlet.http.HttpServletRequest

class AuthRequestInterceptor(private val signingSecret: String, private val envConfig: EnvConfig) : PreForwardRequestInterceptor {

    // Use a map for constant time lookups. Value doesn't matter
    // Hypothetically these should be universally unique, so we don't have to limit by env
    private val bannedUsers: HashMap<String?, String?> = object : HashMap<String?, String?>() {
        init {
            put("d7b9dbed-9719-4856-5f19-23da2d0e3dec", "hidden")
        }
    }

    override fun intercept(data: RequestData, mapping: MappingProperties) {
        // sanitize incoming requests and set authorization information
        val authorization = setAuthHeader(data, mapping)
        validateRestrict(mapping)
        validateSecurity(data, mapping, authorization)

        // TODO - filter restricted headers
    }

    private fun setAuthHeader(data: RequestData, mapping: MappingProperties): String {
        // default to anonymous web when prove otherwise
        var authorization = AuthConstant.AUTHORIZATION_ANONYMOUS_WEB
        val headers = data.headers
        val session = getSession(data.originRequest)
        if (session != null) {
            authorization = if (session.support) {
                AuthConstant.AUTHORIZATION_SUPPORT_USER
            } else {
                AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
            }
            checkBannedUsers(session.userId!!)
            headers[AuthConstant.CURRENT_USER_HEADER] = session.userId
        } else {
            // prevent hacking
            headers.remove(AuthConstant.CURRENT_USER_HEADER)
        }
        headers[AuthConstant.AUTHORIZATION_HEADER] = authorization
        return authorization
    }

    private fun checkBannedUsers(userId: String) {
        if (bannedUsers.containsKey(userId)) {
            log.warn(String.format("Banned user accessing service - user %s", userId))
            throw ForbiddenException("Banned user forbidden!")
        }
    }

    private fun getService(mapping: MappingProperties): Service {
        val host = mapping.host
        val subDomain = host.replace("." + envConfig.externalApex, "")
        return ServiceDirectory.mapping?.get(subDomain.toLowerCase())
                ?: throw FaradayException("Unsupported sub-domain $subDomain")
    }

    private fun validateRestrict(mapping: MappingProperties) {
        val service = getService(mapping)
        if (service.restrictDev && !envConfig.debug) {
            throw FaradayException("This service is restrict to dev and test environment only")
        }
    }

    // check response Authorization and see if it's ok
    // with the requested service
    private fun validateSecurity(data: RequestData, mapping: MappingProperties, authorization: String) {
        // Check perimeter authorization
        if (AuthConstant.AUTHORIZATION_ANONYMOUS_WEB == authorization) {
            val service = getService(mapping)
            if (SecurityConstant.SEC_PUBLIC != service.security) {
                log.info("Anonymous user want to access secure service, redirect to login")
                // send to login
                var scheme = "https"
                if (envConfig.debug) {
                    scheme = "http"
                }
                val port = data.originRequest.serverPort
                try {
                    val redirectUrl = URI(scheme,
                            null,
                            "www." + envConfig.externalApex,
                            port,
                            "/login/", null, null)
                    val returnTo = data.host + data.uri
                    val fullRedirectUrl = "$redirectUrl?return_to=$returnTo"
                    data.isNeedRedirect = true
                    data.redirectUrl = fullRedirectUrl
                } catch (e: URISyntaxException) {
                    log.error("Fail to build redirect url", e)
                }
            }
        }
    }

    private fun getSession(request: HttpServletRequest): Session? {
        val token = getToken(request) ?: return null
        return try {
            val decodedJWT: DecodedJWT = verifySessionToken(token, signingSecret)
            val userId: String = decodedJWT.getClaim(Sign.CLAIM_USER_ID).asString()
            val support: Boolean = decodedJWT.getClaim(Sign.CLAIM_SUPPORT).asBoolean()
            Session(userId, support)
        } catch (e: Exception) {
            log.error("fail to verify token", "token", token, e)
            null
        }
    }

    private data class Session(
            val userId: String? = null,
            val support: Boolean = false
    )

    companion object {
        private val log: ILogger = SLoggerFactory.getLogger(AuthRequestInterceptor::class.java)
    }


}