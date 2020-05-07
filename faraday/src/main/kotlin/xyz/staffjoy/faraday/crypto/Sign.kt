package xyz.staffjoy.common.crypto

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.util.StringUtils
import xyz.staffjoy.faraday.exceptions.ServiceException
import java.util.*
import java.util.concurrent.TimeUnit

object Sign {
    const val CLAIM_EMAIL = "email"
    const val CLAIM_USER_ID = "userId"
    const val CLAIM_SUPPORT = "support"
    private val verifierMap: MutableMap<String, JWTVerifier?> = HashMap()
    private val algorithmMap: MutableMap<String, Algorithm?> = HashMap()
    private fun getAlgorithm(signingToken: String): Algorithm? {
        var algorithm = algorithmMap[signingToken]
        if (algorithm == null) {
            synchronized(algorithmMap) {
                algorithm = algorithmMap[signingToken]
                if (algorithm == null) {
                    algorithm = Algorithm.HMAC512(signingToken)
                    algorithmMap[signingToken] = algorithm
                }
            }
        }
        return algorithm
    }

    fun generateEmailConfirmationToken(userId: String?, email: String?, signingToken: String): String {
        val algorithm = getAlgorithm(signingToken)
        return JWT.create()
                .withClaim(CLAIM_EMAIL, email)
                .withClaim(CLAIM_USER_ID, userId)
                .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)))
                .sign(algorithm)
    }

    fun verifyEmailConfirmationToken(tokenString: String?, signingToken: String): DecodedJWT {
        return verifyToken(tokenString, signingToken)
    }

    fun verifySessionToken(tokenString: String?, signingToken: String): DecodedJWT {
        return verifyToken(tokenString, signingToken)
    }

    fun verifyToken(tokenString: String?, signingToken: String): DecodedJWT {
        var verifier = verifierMap[signingToken]
        if (verifier == null) {
            synchronized(verifierMap) {
                verifier = verifierMap[signingToken]
                if (verifier == null) {
                    val algorithm = Algorithm.HMAC512(signingToken)
                    verifier = JWT.require(algorithm).build()
                    verifierMap[signingToken] = verifier
                }
            }
        }
        return verifier!!.verify(tokenString)
    }

    fun generateSessionToken(userId: String?, signingToken: String, support: Boolean, duration: Long): String {
        if (StringUtils.isEmpty(signingToken)) {
            throw ServiceException("No signing token present")
        }
        val algorithm = getAlgorithm(signingToken)
        return JWT.create()
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_SUPPORT, support)
                .withExpiresAt(Date(System.currentTimeMillis() + duration))
                .sign(algorithm)
    }
}