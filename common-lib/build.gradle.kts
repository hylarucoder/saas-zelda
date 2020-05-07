plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.0.RELEASE")

    implementation("io.sentry:sentry:1.7.17")
    implementation("structlog4j:structlog4j-api:1.0.0")
    implementation("structlog4j:structlog4j-json:1.0.0")
    implementation("org.modelmapper:modelmapper:2.3.3")
    implementation("com.auth0:java-jwt:3.6.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
}
