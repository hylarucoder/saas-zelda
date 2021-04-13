package xyz.zelda.gateway.core.utils

import java.nio.charset.Charset

object BodyConverter {
    fun convertBodyToString(body: ByteArray): String {
        return String(body, Charset.forName("UTF-8"))
    }

    fun convertStringToBody(body: String): ByteArray {
        return body.toByteArray(Charset.forName("UTF-8"))
    }
}