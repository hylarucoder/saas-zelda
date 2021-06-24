package xyz.zelda.gateway.core.interceptor

import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.gateway.core.http.ResponseData

interface PostForwardResponseInterceptor {
    fun intercept(data: ResponseData, mapping: MappingProperties)
}