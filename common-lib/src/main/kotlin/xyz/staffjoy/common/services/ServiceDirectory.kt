package xyz.staffjoy.common.services

import java.util.*

/**
 * ServiceDirectory allows access to a backend service using its subdomain
 *
 * StaffjoyServices ia a map of subdomains -> specs
 * Subdomain is <string> + Env["rootDomain"]
 * e.g. "login" service on prod is "login" + "staffjoy.xyz"
 *
 * KEEP THIS LIST IN ALPHABETICAL ORDER PLEASE
</string> */
object ServiceDirectory {
    var mapping: Map<String, Service>? = null

    init {
        val map: Map<String, Service> = TreeMap()
        val service: Service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("account-service")
                .build()
        xyz.staffjoy.common.services.map.put("account", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("app-service")
                .noCacheHtml(true)
                .build()
        xyz.staffjoy.common.services.map.put("app", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("company-service")
                .build()
        xyz.staffjoy.common.services.map.put("company", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder() // Debug site for faraday proxy
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(true)
                .backendDomain("httpbin.org")
                .build()
        xyz.staffjoy.common.services.map.put("faraday", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(false)
                .backendDomain("ical-service")
                .build()
        xyz.staffjoy.common.services.map.put("ical", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("myaccount-service")
                .noCacheHtml(true)
                .build()
        xyz.staffjoy.common.services.map.put("myaccount", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(true)
                .backendDomain("superpowers-service")
                .build()
        xyz.staffjoy.common.services.map.put("superpowers", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_AUTHENTICATED)
                .restrictDev(false)
                .backendDomain("whoami-service")
                .build()
        xyz.staffjoy.common.services.map.put("whoami", xyz.staffjoy.common.services.service)
        xyz.staffjoy.common.services.service = Service.builder()
                .security(SecurityConstant.SEC_PUBLIC)
                .restrictDev(false)
                .backendDomain("www-service")
                .build()
        xyz.staffjoy.common.services.map.put("www", xyz.staffjoy.common.services.service)
        mapping = Collections.unmodifiableMap(xyz.staffjoy.common.services.map)
    }
}