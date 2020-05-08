package xyz.zelda.faraday.core.http

import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClientBuilder.create
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import xyz.zelda.faraday.config.MappingProperties

class HttpClientProvider {
    protected var httpClients: HashMap<String, RestTemplate> = HashMap()
    fun updateHttpClients(mappings: List<MappingProperties>) {
        for (mapping in mappings){
            httpClients[mapping.name] = createRestTemplate(mapping)
        }
    }

    fun getHttpClient(mappingName: String): RestTemplate? {
        return httpClients[mappingName]
    }

    protected fun createRestTemplate(mapping: MappingProperties): RestTemplate {
        val client = createHttpClient(mapping).build()
        val requestFactory = HttpComponentsClientHttpRequestFactory(client)
        requestFactory.setConnectTimeout(mapping.timeout.connect)
        requestFactory.setReadTimeout(mapping.timeout.read)
        return RestTemplate(requestFactory)
    }

    protected fun createHttpClient(mapping: MappingProperties?): HttpClientBuilder {
        return create().useSystemProperties().disableRedirectHandling().disableCookieManagement()
    }
}
