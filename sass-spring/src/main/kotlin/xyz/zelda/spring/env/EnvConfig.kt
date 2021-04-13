package xyz.zelda.spring.env

enum class ENV(val env: String) {
    DEV("DEV"),
    TEST("TEST"),
    UAT("UAT"),
    PROD("PROD")
}

data class EnvConfig(val name: String?, val debug: Boolean = false, val externalApex: String?, val internalApex: String?, val scheme: String?) {

    companion object {
        private val hashMap = HashMap<String, EnvConfig>()

        fun getEnvConfig(profile: String): EnvConfig? {
            hashMap[ENV.DEV.name] = EnvConfig(
                    ENV.DEV.name,
                    true,
                    "zelda.local",
                    ENV.DEV.name,
                    "http"
            )
            hashMap[ENV.TEST.name] = EnvConfig(
                    ENV.TEST.name,
                    true,
                    "zelda.local",
                    ENV.TEST.name,
                    "http"
            )
            hashMap[ENV.UAT.name] = EnvConfig(
                    ENV.UAT.name,
                    true,
                    "zelda-uat.local",
                    ENV.UAT.name,
                    "http"
            )
            hashMap[ENV.PROD.name] = EnvConfig(
                    ENV.PROD.name,
                    true,
                    "zelda.com",
                    ENV.PROD.name,
                    "http"
            )
            return hashMap[profile]
        }
    }
}




