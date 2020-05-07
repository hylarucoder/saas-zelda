package xyz.staffjoy.faraday.env

enum class ENV(val env: String) {
    DEV("dev"),
    TEST("test"),
    UAT("uat"),
    PROD("prod")
}

data class EnvConfig(val name: String?, val debug: Boolean = false, val externalApex: String?, val internalApex: String?, val scheme: String?)


