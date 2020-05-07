package xyz.staffjoy.faraday.core.trace

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import xyz.staffjoy.faraday.config.FaradayProperties
import java.util.*

class ProxyingTraceInterceptor(protected val faradayProperties: FaradayProperties, protected val traceInterceptor: TraceInterceptor) {
    fun generateTraceId(): String? {
        return if (faradayProperties.tracing.enabled) UUID.randomUUID().toString() else null
    }

    fun onRequestReceived(traceId: String?, method: HttpMethod, host: String, uri: String, headers: HttpHeaders) {
        runIfTracingIsEnabled(Runnable {
            val request = getIncomingRequest(method, host, uri, headers)
            traceInterceptor.onRequestReceived(traceId, request)
        })
    }

    private fun getIncomingRequest(method: HttpMethod, host: String, uri: String, headers: HttpHeaders): IncomingRequest {
        val request = IncomingRequest()
        request.method = method
        request.host = host
        request.uri = uri
        request.headers = headers
        return request
    }

    fun onNoMappingFound(traceId: String?, method: HttpMethod, host: String, uri: String, headers: HttpHeaders) {
        runIfTracingIsEnabled(Runnable {
            val request = getIncomingRequest(method, host, uri, headers)
            traceInterceptor.onNoMappingFound(traceId, request)
        })
    }

    fun onForwardStart(traceId: String?, mappingName: String?, method: HttpMethod?, host: String?, uri: String?, body: ByteArray?, headers: HttpHeaders?) {
        runIfTracingIsEnabled(Runnable {
            val request = ForwardRequest()
            request.mappingName = mappingName
            request.method = method
            request.host = host
            request.uri = uri
            request.body = body!!
            request.headers = headers
            traceInterceptor.onForwardStart(traceId, request)
        })
    }

    fun onForwardFailed(traceId: String?, error: Throwable?) {
        runIfTracingIsEnabled(Runnable { traceInterceptor.onForwardError(traceId, error) })
    }

    fun onForwardComplete(traceId: String?, status: HttpStatus?, body: ByteArray?, headers: HttpHeaders?) {
        runIfTracingIsEnabled(Runnable {
            val response = ReceivedResponse()
            response.status = status
            if (body != null) {
                response.body = body
            }
            response.headers = headers
            traceInterceptor.onForwardComplete(traceId, response)
        })
    }

    protected fun runIfTracingIsEnabled(operation: Runnable) {
        if (faradayProperties.tracing.enabled) {
            operation.run()
        }
    }

}