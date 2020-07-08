plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("io.github.microutils:kotlin-logging:1.7.10")

    //    implementation("com.fasterxml.jackson.core:jackson-annotations")
    //    implementation("com.fasterxml.jackson.core:jackson-databind")
    //    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hppc")
    //    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    //    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
    //    implementation("com.zaxxer:HikariCP")
    //    implementation("io.dropwizard.metrics:metrics-core")
    //    implementation("io.jsonwebtoken:jjwt-api")
    //    implementation("io.micrometer:micrometer-registry-prometheus")
    //    implementation("javax.annotation:javax.annotation-api")
    //    implementation("javax.transaction:javax.transaction-api")

    //    implementation("org.springframework.security:spring-security-config:2.3.0.RELEASE")
    //    implementation("org.springframework.security:spring-security-data:2.3.0.RELEASE")
    //    implementation("org.springframework.security:spring-security-web:2.3.0.RELEASE")


    implementation("com.auth0:java-jwt:3.6.0")
    implementation("io.sentry:sentry:1.7.17")
}
