package xyz.staffjoy.common.env

import lombok.*
import java.util.*

// environment related configuration
@Data
@Builder
class EnvConfig {
    private val name: String? = null
    private val debug = false
    private val externalApex: String? = null
    private val internalApex: String? = null
    private val scheme: String? = null

    companion object {
        @Getter(AccessLevel.NONE)
        @Setter(AccessLevel.NONE)
        private var map: MutableMap<String, EnvConfig>? = null
        fun getEnvConfg(env: String): EnvConfig? {
            var envConfig = map!![env]
            if (envConfig == null) {
                envConfig = map!![EnvConstant.ENV_DEV]
            }
            return envConfig
        }

        init {
            map = HashMap()
            val envConfig: EnvConfig = builder().name(EnvConstant.ENV_DEV)
                    .debug(true)
                    .externalApex("staffjoy-v2.local")
                    .internalApex(EnvConstant.ENV_DEV)
                    .scheme("http")
                    .build()
            map[EnvConstant.ENV_DEV] = xyz.staffjoy.common.env.envConfig
            xyz.staffjoy.common.env.envConfig = builder().name(EnvConstant.ENV_TEST)
                    .debug(true)
                    .externalApex("staffjoy-v2.local")
                    .internalApex(EnvConstant.ENV_DEV)
                    .scheme("http")
                    .build()
            map[EnvConstant.ENV_TEST] = xyz.staffjoy.common.env.envConfig

            // for aliyun k8s demo, enable debug and use http and staffjoy-uat.local
            // in real world, disable debug and use http and staffjoy-uat.xyz in UAT environment
            xyz.staffjoy.common.env.envConfig = builder().name(EnvConstant.ENV_UAT)
                    .debug(true)
                    .externalApex("staffjoy-uat.local")
                    .internalApex(EnvConstant.ENV_UAT)
                    .scheme("http")
                    .build()
            map[EnvConstant.ENV_UAT] = xyz.staffjoy.common.env.envConfig

//        envConfig = EnvConfig.builder().name(EnvConstant.ENV_UAT)
//                .debug(false)
//                .externalApex("staffjoy-uat.xyz")
//                .internalApex(EnvConstant.ENV_UAT)
//                .scheme("https")
//                .build();
//        map.put(EnvConstant.ENV_UAT, envConfig);
            xyz.staffjoy.common.env.envConfig = builder().name(EnvConstant.ENV_PROD)
                    .debug(false)
                    .externalApex("staffjoy.com")
                    .internalApex(EnvConstant.ENV_PROD)
                    .scheme("https")
                    .build()
            map[EnvConstant.ENV_PROD] = xyz.staffjoy.common.env.envConfig
        }
    }
}