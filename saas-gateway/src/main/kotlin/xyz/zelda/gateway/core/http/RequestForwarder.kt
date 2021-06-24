package xyz.zelda.gateway.core.http

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import xyz.zelda.faraday.config.FaradayProperties
import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.gateway.core.balancer.LoadBalancer
import xyz.zelda.gateway.core.interceptor.PostForwardResponseInterceptor
import xyz.zelda.gateway.core.mappings.MappingsProvider
import xyz.zelda.gateway.core.trace.ProxyingTraceInterceptor
import xyz.zelda.gateway.exceptions.FaradayException
import java.net.URI
import java.net.URISyntaxException
import java.time.Duration
import java.util.function.Consumer

class RequestForwarder(
    protected val serverProperties: ServerProperties,
    protected val faradayProperties: FaradayProperties,
    protected val httpClientProvider: HttpClientProvider,
    protected val mappingsProvider: MappingsProvider,
    protected val loadBalancer: LoadBalancer,
    meterRegistry: MeterRegistry,
    traceInterceptor: ProxyingTraceInterceptor,
    postForwardResponseInterceptor: PostForwardResponseInterceptor
) {
    protected val meterRegistry: MeterRegistry
    protected val traceInterceptor: ProxyingTraceInterceptor
    protected val postForwardResponseInterceptor: PostForwardResponseInterceptor
    fun forwardHttpRequest(data: RequestData, traceId: String, mapping: MappingProperties): ResponseEntity<ByteArray>? {
        val destination = resolveForwardDestination(data.uri, mapping)
        prepareForwardedRequestHeaders(data, destination)
        traceInterceptor.onForwardStart(traceId, destination.mappingName,
                data.method, data.host, destination.uri.toString(),
                data.body, data.headers)
        val request: RequestEntity<ByteArray> = RequestEntity<ByteArray>(data.body, data.headers, data.method, destination.uri)
        val response = sendRequest(traceId, request, mapping, destination.mappingMetricsName, data)
        log.debug(String.format("Forwarded: %s %s %s -> %s %d", data.method, data.host, data.uri, destination.uri, response?.status))
        traceInterceptor.onForwardComplete(traceId, response?.status, response?.body, response?.headers)
        if (response != null) {
            postForwardResponseInterceptor.intercept(response, mapping)
            prepareForwardedResponseHeaders(response)
        }
        return response?.status?.let {
            ResponseEntity.status(it)
                    .headers(response.headers)
                    .body(response.body)
        }
    }

    /**
     * Remove any protocol-level headers from the remote server's response that
     * do not apply to the new response we are sending.
     *
     * @param response
     */

    protected fun prepareForwardedResponseHeaders(response: ResponseData) {
        val headers = response.headers
        headers.remove(HttpHeaders.TRANSFER_ENCODING)
        headers.remove(HttpHeaders.CONNECTION)
        headers.remove("Public-Key-Pins")
        headers.remove(HttpHeaders.SERVER)
        headers.remove("Strict-Transport-Security")
    }

    /**
     * Remove any protocol-level headers from the clients request that
     * do not apply to the new request we are sending to the remote server.
     *
     * @param request
     * @param destination
     */

    protected fun prepareForwardedRequestHeaders(request: RequestData, destination: ForwardDestination?) {
        val headers: HttpHeaders = request.headers
        //headers.set(HOST, destination.getUri().getAuthority());
        headers.remove(HttpHeaders.TE)
    }

    protected fun resolveForwardDestination(originUri: String, mapping: MappingProperties): ForwardDestination {
        return ForwardDestination(createDestinationUrl(originUri, mapping), mapping.name, resolveMetricsName(mapping))
    }

    protected fun createDestinationUrl(uri: String, mapping: MappingProperties): URI {
        val host = loadBalancer.chooseDestination(mapping.destinations)
        return try {
            URI(host + uri)
        } catch (e: URISyntaxException) {
            throw FaradayException("Error creating destination URL from HTTP request URI: $uri using mapping $mapping", e)
        }
    }

    protected fun sendRequest(traceId: String, request: RequestEntity<ByteArray>, mapping: MappingProperties, mappingMetricsName: String?, requestData: RequestData): ResponseData? {
        var response: ResponseEntity<ByteArray?>
        val startingTime = System.nanoTime()
        try {
            response = httpClientProvider.getHttpClient(mapping.name)!!.exchange(request, ByteArray::class.java)
            recordLatency(mappingMetricsName, startingTime)
        } catch (e: HttpStatusCodeException) {
            recordLatency(mappingMetricsName, startingTime)
            response = ResponseEntity.status(e.statusCode)
                    .headers(e.responseHeaders)
                    .body(e.responseBodyAsByteArray)
        } catch (e: Exception) {
            recordLatency(mappingMetricsName, startingTime)
            traceInterceptor.onForwardFailed(traceId, e)
            throw e
        }
        val data = UnmodifiableRequestData(requestData)
        return response.body?.let { ResponseData(response.statusCode, response.headers, it, data) }
    }


    protected fun recordLatency(metricName: String?, startingTime: Long) {

        Consumer<MeterRegistry> { meterRegistry: MeterRegistry ->
            if (metricName != null) {
                meterRegistry.timer(metricName).record(Duration.ofNanos(System.nanoTime() - startingTime))
            }
        }
    }

    protected fun resolveMetricsName(mapping: MappingProperties): String {
        return faradayProperties.metrics.namesPrefix + "." + mapping.name
    }

    companion object {
        private val log: ILogger = SLoggerFactory.getLogger(RequestForwarder::class.java)
    }

    init {
        this.meterRegistry = meterRegistry
        this.traceInterceptor = traceInterceptor
        this.postForwardResponseInterceptor = postForwardResponseInterceptor
    }
}
