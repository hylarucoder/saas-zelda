package xyz.staffjoy.common.services

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * Service is an app on Staffjoy that runs on a subdomain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Service {
    private val security // Public, Authenticated, or Admin
            = 0
    private val restrictDev // If true, service is suppressed in stage and prod
            = false
    private val backendDomain // Backend service to query
            : String? = null
    private val noCacheHtml // If true, injects a header for HTML responses telling the browser not to cache HTML
            = false
}