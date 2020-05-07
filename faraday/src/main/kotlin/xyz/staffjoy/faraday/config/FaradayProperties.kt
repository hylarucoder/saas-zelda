//package xyz.staffjoy.faraday.config
//
//import org.springframework.boot.context.properties.ConfigurationProperties
//import org.springframework.boot.context.properties.NestedConfigurationProperty
//import org.springframework.core.Ordered
//import java.util.*
//
///**
// * Faraday configuration properties
// */
//@ConfigurationProperties("faraday")
//class FaradayProperties {
//    /**
//     * Faraday servlet filter order.
//     */
//    var filterOrder = Ordered.HIGHEST_PRECEDENCE + 100
//
//    /**
//     * Enable programmatic mapping or not,
//     * false only in dev environment, in dev we use mapping via configuration file
//     */
//    var isEnableProgrammaticMapping = true
//
//    /**
//     * Properties responsible for collecting metrics during HTTP requests forwarding.
//     */
//    @NestedConfigurationProperty
//    var metrics = MetricsProperties()
//
//    /**
//     * Properties responsible for tracing HTTP requests proxying processes.
//     */
//    @NestedConfigurationProperty
//    var tracing = TracingProperties()
//
//    /**
//     * List of proxy mappings.
//     */
//    @NestedConfigurationProperty
//    var mappings: List<MappingProperties> = ArrayList()
//
//}