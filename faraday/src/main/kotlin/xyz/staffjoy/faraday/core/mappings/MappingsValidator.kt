package xyz.staffjoy.faraday.core.mappings

import org.apache.commons.lang3.StringUtils.*
import org.springframework.util.CollectionUtils
import xyz.staffjoy.faraday.config.MappingProperties
import xyz.staffjoy.faraday.exceptions.FaradayException
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

class MappingsValidator {
    fun validate(mappings: List<MappingProperties?>) {
        if (!CollectionUtils.isEmpty(mappings)) {
            mappings.forEach(Consumer { mapping: MappingProperties? -> correctMapping(mapping) })
            val numberOfNames = mappings.stream()
                    .map<String>(MappingProperties::name)
                    .collect(Collectors.toSet())
                    .size
            if (numberOfNames < mappings.size) {
                throw FaradayException("Duplicated route names in mappings")
            }
            val numberOfHosts = mappings.stream()
                    .map<String>(MappingProperties::host)
                    .collect(Collectors.toSet())
                    .size
            if (numberOfHosts < mappings.size) {
                throw FaradayException("Duplicated source hosts in mappings")
            }
            mappings.sort(java.util.Comparator { mapping1: MappingProperties, mapping2: MappingProperties -> mapping2.host.compareTo(mapping1.host) })
        }
    }

    protected fun correctMapping(mapping: MappingProperties?) {
        validateName(mapping)
        validateDestinations(mapping)
        validateHost(mapping)
        validateTimeout(mapping)
    }

    protected fun validateName(mapping: MappingProperties?) {
        if (isBlank(mapping!!.name)) {
            throw FaradayException("Empty name for mapping $mapping")
        }
    }

    protected fun validateDestinations(mapping: MappingProperties?) {
        if (CollectionUtils.isEmpty(mapping!!.destinations)) {
            throw FaradayException("No destination hosts for mapping$mapping")
        }
        val correctedHosts: MutableList<String> = ArrayList(mapping.destinations!!.size)
        mapping.destinations!!.forEach(Consumer { destination: String ->
            if (isBlank(destination)) {
                throw FaradayException("Empty destination for mapping $mapping")
            }
            if (!destination.matches(".+://.+")) {
                destination = "http://$destination"
            }
            destination = removeEnd(destination, "/")
            correctedHosts.add(destination)
        })
        mapping.destinations = correctedHosts
    }

    protected fun validateHost(mapping: MappingProperties?) {
        if (isBlank(mapping!!.host)) {
            throw FaradayException("No source host for mapping $mapping")
        }
    }

    protected fun validateTimeout(mapping: MappingProperties?) {
        val connectTimeout = mapping!!.timeout.connect
        if (connectTimeout < 0) {
            throw FaradayException("Invalid connect timeout value: $connectTimeout")
        }
        val readTimeout = mapping.timeout.read
        if (readTimeout < 0) {
            throw FaradayException("Invalid read timeout value: $readTimeout")
        }
    }
}