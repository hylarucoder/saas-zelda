package xyz.zelda.gateway.core.trace

import org.springframework.http.HttpMethod
import xyz.zelda.gateway.core.trace.HttpEntity

open class IncomingRequest : HttpEntity() {
    var method: HttpMethod? = null
    var uri: String? = null
    var host: String? = null

}