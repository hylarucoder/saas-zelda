//package xyz.staffjoy.faraday.core.mappings
//
//import org.springframework.boot.autoconfigure.web.ServerProperties
//import xyz.staffjoy.common.env.EnvConfig
//import xyz.staffjoy.common.services.ServiceDirectory.mapping
//import xyz.staffjoy.faraday.config.FaradayProperties
//import xyz.staffjoy.faraday.config.MappingProperties
//import xyz.staffjoy.faraday.core.http.HttpClientProvider
//import java.util.*
//import javax.servlet.http.HttpServletRequest
//
//class ProgrammaticMappingsProvider(
//        protected val envConfig: EnvConfig,
//        serverProperties: ServerProperties?,
//        faradayProperties: FaradayProperties?,
//        mappingsValidator: MappingsValidator?,
//        httpClientProvider: HttpClientProvider?
//) : MappingsProvider(serverProperties!!, faradayProperties!!, mappingsValidator!!, httpClientProvider!!) {
//    override fun shouldUpdateMappings(request: HttpServletRequest?): Boolean {
//        return false
//    }
//
//    override fun retrieveMappings(): List<MappingProperties?> {
//        val mappings: MutableList<MappingProperties?> = ArrayList()
//        val serviceMap = mapping
//        for (key in serviceMap!!.keys) {
//            val subDomain = key.toLowerCase()
//            val service = serviceMap[key]
//            val mapping = MappingProperties()
//            mapping.name = subDomain + "_route"
//            mapping.host = subDomain + "." + envConfig.getExternalApex()
//            // No security on backend right now :-(
//            val dest = "http://" + service.getBackendDomain()
//            mapping.destinations = Arrays.asList(dest)
//            mappings.add(mapping)
//        }
//        return mappings
//    }
//
//}