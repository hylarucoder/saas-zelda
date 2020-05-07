package xyz.staffjoy.faraday.core.mappings

import org.springframework.boot.autoconfigure.web.ServerProperties
import xyz.staffjoy.faraday.config.FaradayProperties
import xyz.staffjoy.faraday.config.MappingProperties
import xyz.staffjoy.faraday.core.http.HttpClientProvider
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class ConfigurationMappingsProvider(
        serverProperties: ServerProperties?,
        faradayProperties: FaradayProperties?,
        mappingsValidator: MappingsValidator?,
        httpClientProvider: HttpClientProvider?
) : MappingsProvider(serverProperties, faradayProperties,
        mappingsValidator, httpClientProvider) {
    override fun shouldUpdateMappings(request: HttpServletRequest): Boolean {
        return false
    }

    override fun retrieveMappings(): List<MappingProperties> {
        return faradayProperties.mappings.stream()
                .map { obj: MappingProperties -> obj.copy() }
                .collect(Collectors.toList())
    }
}