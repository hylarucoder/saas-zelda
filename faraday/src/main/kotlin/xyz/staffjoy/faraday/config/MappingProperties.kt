package xyz.staffjoy.faraday.config

import java.util.*

class TimeoutProperties(

        /**
         * Connect timeout for HTTP requests forwarding.
         */
        var connect: Int = 2000,

        /**
         * Read timeout for HTTP requests forwarding.
         */
        var read: Int = 20000
)

class MappingProperties(

        /**
         * Name of the mapping
         */
        var name: String? = null,

        /**
         * Path for mapping incoming HTTP requests URIs.
         */
        var host: String = "",

        /**
         * List of destination hosts where HTTP requests will be forwarded.
         */
        var destinations: List<String>? = ArrayList(),

        /**
         * Properties responsible for timeout while forwarding HTTP requests.
         */
        var timeout: TimeoutProperties,

        /**
         * Custom properties placeholder.
         */
        var customConfiguration: Map<String, Any>? = HashMap()

)
