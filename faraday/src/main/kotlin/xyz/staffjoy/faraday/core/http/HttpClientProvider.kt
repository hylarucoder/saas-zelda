package xyz.staffjoy.faraday.core.http

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClientBuilder.create
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import xyz.staffjoy.faraday.config.MappingProperties
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

class HttpClientProvider {
    protected var httpClients: Map<String, RestTemplate> = HashMap()
    fun updateHttpClients(mappings: List<MappingProperties?>) {
        httpClients = mappings.stream().collect(Collectors.toMap(MappingProperties::name, Function { mapping: MappingProperties -> createRestTemplate(mapping) }))
    }

    fun getHttpClient(mappingName: String): RestTemplate? {
        return httpClients[mappingName]
    }

    protected fun createRestTemplate(mapping: MappingProperties): RestTemplate {
        val client: CloseableHttpClient = createHttpClient(mapping).build()
        val requestFactory = HttpComponentsClientHttpRequestFactory(client)
        requestFactory.setConnectTimeout(mapping.timeout.connect)
        requestFactory.setReadTimeout(mapping.timeout.read)
        return RestTemplate(requestFactory)
    }

    protected fun createHttpClient(mapping: MappingProperties?): HttpClientBuilder {
        return create().useSystemProperties().disableRedirectHandling().disableCookieManagement()
    }
}