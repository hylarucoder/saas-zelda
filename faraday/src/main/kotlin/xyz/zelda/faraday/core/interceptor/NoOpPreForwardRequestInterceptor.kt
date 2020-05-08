package xyz.zelda.faraday.core.interceptor

import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.RequestData

class NoOpPreForwardRequestInterceptor : PreForwardRequestInterceptor {
    override fun intercept(data: RequestData, mapping: MappingProperties) {}
}