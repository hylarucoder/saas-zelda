plugins {
    kotlin("jvm")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    api("io.github.microutils:kotlin-logging:1.7.10")
    api("io.sentry:sentry:1.7.17")
    api("com.aliyun:aliyun-java-sdk-core:3.7.1")
    api("com.aliyun:aliyun-java-sdk-dysmsapi:1.1.0")
    api("com.auth0:java-jwt:3.6.0")
    api("io.sentry:sentry:1.7.17")
    api("structlog4j:structlog4j-api:1.0.0")
    api("structlog4j:structlog4j-json:1.0.0")
    api("org.modelmapper:modelmapper:2.3.3")
    //    implementation("com.zaxxer:HikariCP")
}
