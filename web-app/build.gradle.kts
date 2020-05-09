plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":infra-lib"))

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.2.0.RELEASE")
    implementation("org.apache.commons:commons-lang3:3.10")


    implementation("structlog4j:structlog4j-api:1.0.0")
    implementation("structlog4j:structlog4j-json:1.0.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.12")
    implementation("com.auth0:java-jwt:3.6.0")
    implementation("io.sentry:sentry:1.7.17")

}
