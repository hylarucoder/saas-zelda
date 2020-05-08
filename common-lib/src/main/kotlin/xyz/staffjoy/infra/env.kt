package xyz.staffjoy.common


enum class ENV(val env: String) {
    DEV("dev"),
    TEST("test"),
    UAT("uat"),
    PROD("prod")
}

data class EnvConfig(val name: String?, val debug: Boolean = false, val externalApex: String?, val internalApex: String?, val scheme: String?){

}


val map = HashMap<String, ENV> ()

//map[ENV.DEV] = EnvConfig(ENV.DEV)
//val envConfig: EnvConfig = builder().name(EnvConstant.ENV_DEV)
//        .debug(true)
//        .externalApex("staffjoy-v2.local")
//        .internalApex(EnvConstant.ENV_DEV)
//        .scheme("http")
//        .build()
//envConfig = builder().name(EnvConstant.ENV_TEST)
//.debug(true)
//.externalApex("staffjoy-v2.local")
//.internalApex(EnvConstant.ENV_DEV)
//.scheme("http")
//.build()
//map[EnvConstant.ENV_TEST] = xyz.staffjoy.infra.env.envConfig
//
//// for aliyun k8s demo, enable debug and use http and staffjoy-uat.local
//// in real world, disable debug and use http and staffjoy-uat.xyz in UAT environment
//envConfig = builder().name(EnvConstant.ENV_UAT)
//.debug(true)
//.externalApex("staffjoy-uat.local")
//.internalApex(EnvConstant.ENV_UAT)
//.scheme("http")
//.build()
//map[EnvConstant.ENV_UAT] = xyz.staffjoy.infra.env.envConfig
//
//envConfig = builder().name(EnvConstant.ENV_PROD)
//.debug(false)
//.externalApex("staffjoy.com")
//.internalApex(EnvConstant.ENV_PROD)
//.scheme("https")
//.build()
//map[EnvConstant.ENV_PROD] = xyz.staffjoy.infra.env.envConfig
