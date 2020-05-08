package xyz.zelda.faraday.core.interceptor

import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.ResponseData

abstract class NoOpPostForwardResponseInterceptor : PostForwardResponseInterceptor {
    override fun intercept(data: ResponseData, mapping: MappingProperties) {}
}