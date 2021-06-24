package xyz.zelda.gateway.core.trace

import org.springframework.http.HttpStatus
import xyz.zelda.gateway.core.utils.BodyConverter

class ReceivedResponse : HttpEntity() {
    var status: HttpStatus? = null
    lateinit var body: ByteArray

    val bodyAsString: String?
        get() = BodyConverter.convertBodyToString(body)

}