package xyz.zelda.faraday.core.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import javax.servlet.http.HttpServletRequest

class RequestData(
        method: HttpMethod,
        host: String,
        uri: String,
        headers: HttpHeaders,
        body: ByteArray,
        request: HttpServletRequest
) : UnmodifiableRequestData(method, host, uri, headers, body, request) {
    var isNeedRedirect = false
    var redirectUrl: String? = null
    override var method: HttpMethod = method
        get() = super.method

    override var uri: String = uri
        get() = super.uri

    override var headers: HttpHeaders = headers
        get() = super.headers

    override var body: ByteArray = body
        get() = super.body

}
