package xyz.zelda.faraday.core.http

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.apache.commons.lang3.StringUtils.isBlank
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.web.filter.OncePerRequestFilter
import xyz.zelda.faraday.config.FaradayProperties
import xyz.zelda.faraday.core.interceptor.PreForwardRequestInterceptor
import xyz.zelda.faraday.core.mappings.MappingsProvider
import xyz.zelda.faraday.core.trace.ProxyingTraceInterceptor
import xyz.zelda.faraday.exceptions.FaradayException
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ReverseProxyFilter(
        protected val faradayProperties: FaradayProperties,
        protected val extractor: RequestDataExtractor,
        protected val mappingsProvider: MappingsProvider,
        protected val requestForwarder: RequestForwarder,
        protected val traceInterceptor: ProxyingTraceInterceptor,
        protected val preForwardRequestInterceptor: PreForwardRequestInterceptor
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val originUri = extractor.extractUri(request)
        val originHost = extractor.extractHost(request)
        log.debug("Incoming request", "method", request.method,
                "host", originHost,
                "uri", originUri)
        val headers = extractor.extractHttpHeaders(request)
        val method = extractor.extractHttpMethod(request)
        val traceId = traceInterceptor.generateTraceId()
        traceInterceptor.onRequestReceived(traceId, method, originHost, originUri, headers)
        val mapping = mappingsProvider.resolveMapping(originHost, request)
        if (mapping == null) {
            traceInterceptor.onNoMappingFound(traceId, method, originHost, originUri, headers)
            log.debug(String.format("Forwarding: %s %s %s -> no mapping found", method, originHost, originUri))
            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.writer.println("Unsupported domain")
            return
        }

        log.debug(String.format("Forwarding: %s %s %s -> %s", method, originHost, originUri, mapping.destinations))
        val body = extractor.extractBody(request)
        addForwardHeaders(request, headers)
        val dataToForward = RequestData(method, originHost, originUri, headers, body, request)
        preForwardRequestInterceptor.intercept(dataToForward, mapping)
        if (dataToForward.isNeedRedirect && !isBlank(dataToForward.redirectUrl)) {
            log.debug(String.format("Redirecting to -> %s", dataToForward.redirectUrl))
            response.sendRedirect(dataToForward.redirectUrl)
            return
        }
        val responseEntity: ResponseEntity<ByteArray> = requestForwarder.forwardHttpRequest(dataToForward, traceId, mapping)!!
        processResponse(response, responseEntity)
    }

    protected fun addForwardHeaders(request: HttpServletRequest, headers: HttpHeaders) {
        var forwordedFor = headers[X_FORWARDED_FOR_HEADER]
        if (CollectionUtils.isEmpty(forwordedFor)) {
            forwordedFor = ArrayList(1)
        }
        forwordedFor!!.add(request.remoteAddr)
        headers[X_FORWARDED_FOR_HEADER] = forwordedFor
        headers[X_FORWARDED_PROTO_HEADER] = request.scheme
        headers[X_FORWARDED_HOST_HEADER] = request.serverName
        headers[X_FORWARDED_PORT_HEADER] = request.serverPort.toString()
    }

    protected fun processResponse(response: HttpServletResponse, responseEntity: ResponseEntity<ByteArray>) {
        response.status = responseEntity.statusCode.value()
        responseEntity.headers.forEach { name: String?, values: List<String?> -> values.forEach(Consumer { value: String? -> response.addHeader(name, value) }) }
        if (responseEntity.body != null) {
            try {
                response.outputStream.write(responseEntity.body)
            } catch (e: IOException) {
                throw FaradayException("Error writing body of HTTP response", e)
            }
        }
    }

    companion object {
        protected const val X_FORWARDED_FOR_HEADER = "X-Forwarded-For"
        protected const val X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto"
        protected const val X_FORWARDED_HOST_HEADER = "X-Forwarded-Host"
        protected const val X_FORWARDED_PORT_HEADER = "X-Forwarded-Port"
        private val log: ILogger = SLoggerFactory.getLogger(ReverseProxyFilter::class.java)
    }

}