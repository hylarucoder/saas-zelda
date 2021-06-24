package xyz.zelda.gateway.core.trace

import org.springframework.http.HttpHeaders

abstract class HttpEntity {
    var headers: HttpHeaders? = null

}