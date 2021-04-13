package xyz.zelda.spring.services

/**
 * Service is an app on Staffjoy that runs on a subdomain
 */
class Service(
        // Public, Authenticated, or Admin
        val security: Int = 0,
        // If true, service is suppressed in stage and prod
        val restrictDev: Boolean = false,
        // Backend service to query
        val backendDomain: String? = null,
        // If true, injects a header for HTML responses telling the browser not to cache HTML
        val noCacheHtml: Boolean = false
)
