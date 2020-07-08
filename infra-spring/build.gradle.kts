plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-loader-tools:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-integration:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-logging:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mustache:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty:2.3.0.RELEASE")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    runtimeOnly("com.h2database:h2:1.3.176")
    //    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch:2.3.0.RELEASE")

    //    implementation("org.springframework.boot:spring-boot-starter-cloud-connectors:2.3.0.RELEASE")
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
    implementation("structlog4j:structlog4j-api:1.0.0")
    implementation("structlog4j:structlog4j-json:1.0.0")
    implementation("org.modelmapper:modelmapper:2.3.3")

    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.3.0.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RELEASE")
}
