//package xyz.staffjoy.faraday.core.trace
//
//interface TraceInterceptor {
//    fun onRequestReceived(traceId: String?, request: IncomingRequest?)
//    fun onNoMappingFound(traceId: String?, request: IncomingRequest?)
//    fun onForwardStart(traceId: String?, request: ForwardRequest?)
//    fun onForwardError(traceId: String?, error: Throwable?)
//    fun onForwardComplete(traceId: String?, response: ReceivedResponse?)
//}