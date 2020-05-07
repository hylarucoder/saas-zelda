plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
//    implementation(project(":common-lib"))

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.boot:spring-boot-starter:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.2.0.RELEASE")

    implementation("io.sentry:sentry:1.7.17")
    implementation("commons-io:commons-io:2.6")
    implementation("structlog4j:structlog4j-api:1.0.0")
    implementation("structlog4j:structlog4j-json:1.0.0")
    implementation("org.modelmapper:modelmapper:2.3.3")
    implementation("com.auth0:java-jwt:3.6.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
}
