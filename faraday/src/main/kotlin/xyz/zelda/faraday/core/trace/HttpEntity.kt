package xyz.zelda.faraday.core.trace

import org.springframework.http.HttpHeaders

abstract class HttpEntity {
    var headers: HttpHeaders? = null

}