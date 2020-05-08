package xyz.zelda.faraday.config

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

data class MappingProperties(

        /**
         * Name of the mapping
         */
        val name: String,

        /**
         * Path for mapping incoming HTTP requests URIs.
         */
        val host: String = "",

        /**
         * List of destination hosts where HTTP requests will be forwarded.
         */
        val destinations: List<String>? = ArrayList(),

        /**
         * Custom properties placeholder.
         */
        val customConfiguration: Map<String, Any>? = HashMap(),
        val timeout: TimeoutProperties = TimeoutProperties()

) {
    fun copy(): MappingProperties {
        return MappingProperties(
                name,
                host,
                destinations,
                customConfiguration
        )
    }
}
