package xyz.staffjoy.common.crypto

import org.bouncycastle.util.encoders.Hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Hash {
    @Throws(Exception::class)
    fun encode(key: String, data: String): String {
        val sha256_HMAC = Mac.getInstance("HmacSHA256")
        val secret_key = SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
        sha256_HMAC.init(secret_key)
        return Hex.toHexString(sha256_HMAC.doFinal(data.toByteArray(charset("UTF-8"))))
    }
}