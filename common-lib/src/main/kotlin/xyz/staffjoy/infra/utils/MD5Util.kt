package xyz.staffjoy.common.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and
import kotlin.experimental.or

// helper from https://en.gravatar.com/site/implement/images/java/
object MD5Util {
    fun hex(array: ByteArray): String {
        val sb = StringBuffer()
        for (i in array.indices) {
            sb.append(Integer.toHexString(((array[i]
                    and 0xFF.toByte()) or 0x100.toByte()).toInt()).substring(1, 3))
        }
        return sb.toString()
    }

    fun md5Hex(message: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            return hex(md.digest(message.toByteArray(charset("CP1252"))))
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: UnsupportedEncodingException) {
        }
        return null
    }
}