package xyz.zelda.gateway.core.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import xyz.zelda.gateway.core.utils.BodyConverter.convertBodyToString
import javax.servlet.http.HttpServletRequest

open class UnmodifiableRequestData(open var method: HttpMethod,
                                   var host: String,
                                   open var uri: String,
                                   open var headers: HttpHeaders,
                                   open var body: ByteArray,
                                   var originRequest: HttpServletRequest
) {

    constructor(requestData: RequestData) : this(
            requestData.method,
            requestData.host,
            requestData.uri,
            requestData.headers,
            requestData.body,
            requestData.originRequest
    ) {
    }

    val bodyAsString: String
        get() = convertBodyToString(body)

}