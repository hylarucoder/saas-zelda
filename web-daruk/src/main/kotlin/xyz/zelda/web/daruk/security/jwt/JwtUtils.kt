package xyz.zelda.web.daruk.security.jwt

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtUtils {
    @Value("\${bezkoder.app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${bezkoder.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0
    fun generateJwtToken(authentication: Authentication): String {
//        val userPrincipal = authentication.getPrincipal() as UserDetailsImpl
//        return Jwts.builder()
//                .setSubject(userPrincipal.username)
//                .setIssuedAt(Date())
//                .setExpiration(Date(Date().time + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact()
        return ""
    }

    fun getUserNameFromJwtToken(token: String?): String {
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject()
        return ""
    }

    fun validateJwtToken(authToken: String?): Boolean {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
//            return true
//        } catch (e: SignatureException) {
//            logger.error("Invalid JWT signature: {}", e.getMessage())
//        } catch (e: MalformedJwtException) {
//            logger.error("Invalid JWT token: {}", e.getMessage())
//        } catch (e: ExpiredJwtException) {
//            logger.error("JWT token is expired: {}", e.getMessage())
//        } catch (e: UnsupportedJwtException) {
//            logger.error("JWT token is unsupported: {}", e.getMessage())
//        } catch (e: IllegalArgumentException) {
//            logger.error("JWT claims string is empty: {}", e.message)
//        }
        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}