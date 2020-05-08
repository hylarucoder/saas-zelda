package xyz.zelda.faraday.core.interceptor

import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.ResponseData

interface PostForwardResponseInterceptor {
    fun intercept(data: ResponseData, mapping: MappingProperties)
}