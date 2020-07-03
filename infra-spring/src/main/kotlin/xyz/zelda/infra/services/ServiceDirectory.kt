package xyz.zelda.infra.services

import java.util.*
import kotlin.collections.HashMap

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
    var mapping: Map<String, Service>

    init {
        val map: HashMap<String, Service> = HashMap()

        map["account"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "account-service",
                true
        )

        map["app"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "app-service",
                true
        )

        map["company"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "company-service",
                true
        )

        map["faraday"] = Service(
                SecurityConstant.SEC_PUBLIC,
                false,
                "httpbin.org",
                true
        )

        map["ical"] = Service(
                SecurityConstant.SEC_PUBLIC,
                false,
                "ical-service",
                true
        )

        map["myaccount"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "myaccount-service",
                true
        )

        map["superpowers"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "superpowers-service",
                true
        )

        map["whoami"] = Service(
                SecurityConstant.SEC_AUTHENTICATED,
                false,
                "whoami-service",
                true
        )


        map["www"] = Service(
                SecurityConstant.SEC_PUBLIC,
                false,
                "www-service",
                true
        )
        mapping = Collections.unmodifiableMap(map)
    }
}