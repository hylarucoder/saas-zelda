package xyz.zelda.faraday.core.mappings

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.util.CollectionUtils
import xyz.zelda.faraday.config.FaradayProperties
import xyz.zelda.faraday.config.MappingProperties
import xyz.zelda.faraday.core.http.HttpClientProvider
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

abstract class MappingsProvider(
        protected val serverProperties: ServerProperties,
        protected val faradayProperties: FaradayProperties,
        protected val mappingsValidator: MappingsValidator,
        protected val httpClientProvider: HttpClientProvider
) {
    protected var mappings: List<MappingProperties?>? = null
    fun resolveMapping(originHost: String, request: HttpServletRequest?): MappingProperties? {
        if (shouldUpdateMappings(request)) {
            updateMappings()
        }
        val resolvedMappings = mappings!!.stream()
                .filter { mapping: MappingProperties? -> originHost.toLowerCase() == mapping!!.host.toLowerCase() }
                .collect(Collectors.toList())
        return if (CollectionUtils.isEmpty(resolvedMappings)) {
            null
        } else resolvedMappings[0]
    }

    @PostConstruct
    @Synchronized
    protected fun updateMappings() {
        val newMappings = retrieveMappings()
        mappingsValidator.validate(newMappings)
        mappings = newMappings
        httpClientProvider.updateHttpClients(mappings as MutableList<MappingProperties>)
        log.info("Destination mappings updated", mappings)
    }

    protected abstract fun shouldUpdateMappings(request: HttpServletRequest?): Boolean
    protected abstract fun retrieveMappings(): MutableList<MappingProperties>

    companion object {
        private val log: ILogger = SLoggerFactory.getLogger(MappingsProvider::class.java)
    }

}