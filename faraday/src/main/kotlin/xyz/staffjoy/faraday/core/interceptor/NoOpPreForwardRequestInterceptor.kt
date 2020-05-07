package xyz.staffjoy.faraday.core.interceptor

import xyz.staffjoy.faraday.config.MappingProperties
import xyz.staffjoy.faraday.core.http.RequestData

class NoOpPreForwardRequestInterceptor : PreForwardRequestInterceptor {
    override fun intercept(data: RequestData, mapping: MappingProperties) {}
}