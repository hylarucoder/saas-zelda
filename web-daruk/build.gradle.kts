plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":infra-spring"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

//    implementation("com.fasterxml.jackson.core:jackson-annotations")
//    implementation("com.fasterxml.jackson.core:jackson-databind")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hppc")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
//    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
//    implementation("com.zaxxer:HikariCP")
//    implementation("io.dropwizard.metrics:metrics-core")
//    implementation("io.micrometer:micrometer-registry-prometheus")
//    implementation("javax.annotation:javax.annotation-api")
//    implementation("javax.transaction:javax.transaction-api")

    implementation("org.springframework.boot:spring-boot-loader-tools:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-cloud-connectors:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-integration:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-logging:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mustache:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.security:spring-security-config:5.3.0.RELEASE")
    implementation("org.springframework.security:spring-security-data:5.3.0.RELEASE")
    implementation("org.springframework.security:spring-security-web:5.3.0.RELEASE")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.21.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.+")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.11.+")
    implementation("com.fasterxml.jackson.module:jackson-datatype-jdk8:2.11.+")

    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.3.0.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RELEASE")
//    testImplementation "io.cucumber:cucumber-junit"
//    testImplementation "io.cucumber:cucumber-spring

}
