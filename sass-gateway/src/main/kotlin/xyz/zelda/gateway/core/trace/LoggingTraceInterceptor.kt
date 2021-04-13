package xyz.zelda.gateway.core.trace

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.apache.commons.lang3.StringUtils.trimToEmpty

class LoggingTraceInterceptor : TraceInterceptor {
    override fun onRequestReceived(traceId: String, request: IncomingRequest) {
        log.info("Incoming HTTP request received:", "traceId", traceId,
                "method", request.method, "host", request.host,
                "uri", request.uri, "headers", request.headers)
    }

    override fun onNoMappingFound(traceId: String, request: IncomingRequest) {
        log.info("No mapping found for incoming HTTP request: ", "traceId", traceId,
                "method", request.method, "host", request.host,
                "uri", request.uri, "headers", request.headers)
    }

    override fun onForwardStart(traceId: String, request: ForwardRequest) {
        log.info("Forwarding HTTP request started: ", "traceId", traceId,
                "mappingName", trimToEmpty(request.mappingName),
                "method", request.method, "host", request.host,
                "uri", request.uri, "body", request.body,
                "headers", request.headers)
    }

    override fun onForwardError(traceId: String, error: Throwable) {
        log.error("Forwarding HTTP request failed: ", "traceId", traceId, error)
    }

    override fun onForwardComplete(traceId: String, response: ReceivedResponse) {
        log.info("Forwarded HTTP response received: ", "traceId", traceId,
                "status", response.status, "body", response.bodyAsString,
                "headers", response.headers)
    }

    companion object {
        private val log: ILogger = SLoggerFactory.getLogger(LoggingTraceInterceptor::class.java)
    }
}