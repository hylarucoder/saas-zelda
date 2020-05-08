package xyz.zelda.faraday.core.trace

import xyz.zelda.faraday.core.utils.BodyConverter

class ForwardRequest : IncomingRequest() {
    var mappingName: String? = null
    lateinit var body: ByteArray

    val bodyAsString: String?
        get() = BodyConverter.convertBodyToString(body)

}