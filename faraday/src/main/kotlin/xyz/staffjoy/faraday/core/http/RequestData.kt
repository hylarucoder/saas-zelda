package xyz.staffjoy.faraday.core.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import xyz.staffjoy.faraday.core.utils.BodyConverter
import javax.servlet.http.HttpServletRequest

class RequestData(method: HttpMethod?,
                  host: String?,
                  uri: String?,
                  headers: HttpHeaders?,
                  body: ByteArray?,
                  request: HttpServletRequest?) : UnmodifiableRequestData(method, host, uri, headers, body, request) {
    var isNeedRedirect = false
    var redirectUrl: String? = null
    var method: HttpMethod?
        get() = super.method

    var uri: String?
        get() = super.uri

    var headers: HttpHeaders?
        get() = super.headers

    var body: ByteArray?
        get() = super.body

    fun setBody(body: String?) {
        this.body = BodyConverter.convertStringToBody(body)
    }

}