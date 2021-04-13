package xyz.zelda.gateway.core.interceptor

import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.gateway.core.http.RequestData

interface PreForwardRequestInterceptor {
    fun intercept(data: RequestData, mapping: MappingProperties)
}