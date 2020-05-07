package xyz.staffjoy.faraday.core.trace

import xyz.staffjoy.faraday.core.utils.BodyConverter

class ForwardRequest : IncomingRequest() {
    var mappingName: String? = null
    var body: ByteArray

    val bodyAsString: String
        get() = BodyConverter.convertBodyToString(body)

}