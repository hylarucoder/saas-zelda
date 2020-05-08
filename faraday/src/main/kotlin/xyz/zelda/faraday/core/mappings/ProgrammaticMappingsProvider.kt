package xyz.zelda.faraday.core.mappings

import org.springframework.boot.autoconfigure.web.ServerProperties
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.services.ServiceDirectory.mapping
import xyz.zelda.faraday.config.FaradayProperties
import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.HttpClientProvider
import java.util.*
import javax.servlet.http.HttpServletRequest

class ProgrammaticMappingsProvider(
        protected val envConfig: EnvConfig,
        serverProperties: ServerProperties,
        faradayProperties: FaradayProperties,
        mappingsValidator: MappingsValidator,
        httpClientProvider: HttpClientProvider
) : MappingsProvider(serverProperties, faradayProperties, mappingsValidator, httpClientProvider) {
    override fun shouldUpdateMappings(request: HttpServletRequest?): Boolean {
        return false
    }

    override fun retrieveMappings(): MutableList<MappingProperties> {
        val mappings: MutableList<MappingProperties> = ArrayList()
        val serviceMap = mapping
        for (key in serviceMap!!.keys) {
            val subDomain = key.toLowerCase()
            val service = serviceMap[key]
            // No security on backend right now :-(
            mappings.add(
                MappingProperties(
                    subDomain + "_route",
                    subDomain + "." + envConfig.externalApex,
                    Arrays.asList("http://" + service!!.backendDomain)
                )
            )
        }
        return mappings
    }

}