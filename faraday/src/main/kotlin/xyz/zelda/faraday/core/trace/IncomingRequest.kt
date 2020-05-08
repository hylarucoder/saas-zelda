package xyz.zelda.faraday.core.trace

import org.springframework.http.HttpMethod

open class IncomingRequest : HttpEntity() {
    var method: HttpMethod? = null
    var uri: String? = null
    var host: String? = null

}