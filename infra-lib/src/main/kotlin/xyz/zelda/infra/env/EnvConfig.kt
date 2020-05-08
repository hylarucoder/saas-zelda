package xyz.zelda.infra.env

enum class ENV(val env: String) {
    DEV("dev"),
    TEST("test"),
    UAT("uat"),
    PROD("prod")
}

data class EnvConfig(val name: String?, val debug: Boolean = false, val externalApex: String?, val internalApex: String?, val scheme: String?) {

    companion object {
        private val hashMap = HashMap<String, EnvConfig>()

        fun getEnvConfig(profile: String): EnvConfig? {
            hashMap[ENV.DEV.name] = EnvConfig(
                    ENV.DEV.name,
                    true,
                    "staffjoy-v2.local",
                    ENV.DEV.name,
                    "http"
            )
            hashMap[ENV.TEST.name] = EnvConfig(
                    ENV.TEST.name,
                    true,
                    "staffjoy-v2.local",
                    ENV.TEST.name,
                    "http"
            )
            hashMap[ENV.UAT.name] = EnvConfig(
                    ENV.UAT.name,
                    true,
                    "staffjoy-uat.local",
                    ENV.UAT.name,
                    "http"
            )
            hashMap[ENV.PROD.name] = EnvConfig(
                    ENV.PROD.name,
                    true,
                    "staffjoy.com",
                    ENV.PROD.name,
                    "http"
            )
            return hashMap[profile]
        }
    }
}




