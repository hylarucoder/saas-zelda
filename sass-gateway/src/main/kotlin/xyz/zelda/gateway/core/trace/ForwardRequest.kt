package xyz.zelda.gateway.core.trace

import xyz.zelda.gateway.core.utils.BodyConverter

class ForwardRequest : IncomingRequest() {
    var mappingName: String? = null
    lateinit var body: ByteArray

    val bodyAsString: String?
        get() = BodyConverter.convertBodyToString(body)

}