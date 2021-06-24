package xyz.zelda.gateway.core.utils

import org.apache.commons.lang3.StringUtils.*

object UriCorrector {
    fun correctUri(uri: String?): String {
        return if (isBlank(uri)) {
            EMPTY
        } else removeEnd(prependIfMissing(uri, "/"), "/")
    }
}