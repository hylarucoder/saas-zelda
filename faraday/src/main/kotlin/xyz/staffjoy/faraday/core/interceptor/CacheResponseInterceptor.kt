//package xyz.staffjoy.faraday.core.interceptor
//
//import org.springframework.http.HttpHeaders
//import xyz.staffjoy.faraday.config.MappingProperties
//import xyz.staffjoy.faraday.core.http.ResponseData
//
//class CacheResponseInterceptor : PostForwardResponseInterceptor {
//    override fun intercept(data: ResponseData, mapping: MappingProperties) {
//        val respHeaders = data.headers
//        if (respHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
//            val values = respHeaders[HttpHeaders.CONTENT_TYPE]
//            if (values!!.contains("text/html")) {
//                // insert header to prevent caching
//                respHeaders[HttpHeaders.CACHE_CONTROL] = "no-cache"
//            }
//        }
//    }
//}