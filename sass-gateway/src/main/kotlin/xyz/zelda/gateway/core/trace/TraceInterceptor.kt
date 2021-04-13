package xyz.zelda.gateway.core.trace

import xyz.zelda.gateway.core.trace.ForwardRequest
import xyz.zelda.gateway.core.trace.IncomingRequest
import xyz.zelda.gateway.core.trace.ReceivedResponse

interface TraceInterceptor {
    fun onRequestReceived(traceId: String, request: IncomingRequest)
    fun onNoMappingFound(traceId: String, request: IncomingRequest)
    fun onForwardStart(traceId: String, request: ForwardRequest)
    fun onForwardError(traceId: String, error: Throwable)
    fun onForwardComplete(traceId: String, response: ReceivedResponse)
}