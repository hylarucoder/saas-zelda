//package xyz.staffjoy.faraday.config
//
//import org.apache.commons.lang3.builder.ToStringBuilder
//import org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE
//import java.util.*
//
//class MappingProperties {
//    /**
//     * Name of the mapping
//     */
//    var name: String? = null
//
//    /**
//     * Path for mapping incoming HTTP requests URIs.
//     */
//    var host = ""
//
//    /**
//     * List of destination hosts where HTTP requests will be forwarded.
//     */
//    var destinations: List<String>? = ArrayList()
//
//    /**
//     * Properties responsible for timeout while forwarding HTTP requests.
//     */
//    var timeout = TimeoutProperties()
//
//    /**
//     * Custom properties placeholder.
//     */
//    var customConfiguration: Map<String, Any>? = HashMap()
//
//    fun copy(): MappingProperties {
//        val clone = MappingProperties()
//        clone.name = name
//        clone.host = host
//        clone.destinations = if (destinations == null) null else ArrayList(destinations)
//        clone.timeout = timeout
//        clone.customConfiguration = if (customConfiguration == null) null else HashMap(customConfiguration)
//        return clone
//    }
//
//    override fun toString(): String {
//        return ToStringBuilder(this, NO_CLASS_NAME_STYLE)
//                .append("name", name)
//                .append("host", host)
//                .append("destinations", destinations)
//                .append("timeout", timeout)
//                .append("customConfiguration", customConfiguration)
//                .toString()
//    }
//
//    class TimeoutProperties {
//        /**
//         * Connect timeout for HTTP requests forwarding.
//         */
//        var connect = 2000
//
//        /**
//         * Read timeout for HTTP requests forwarding.
//         */
//        var read = 20000
//
//        override fun toString(): String {
//            return ToStringBuilder(this, NO_CLASS_NAME_STYLE)
//                    .append("connect", connect)
//                    .append("read", read)
//                    .toString()
//        }
//    }
//}