plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":infra-spring"))
    implementation(project(":api-picasso"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.2.0.RELEASE")
    implementation("com.aliyun:aliyun-java-sdk-core:3.7.1")
    implementation("com.aliyun:aliyun-java-sdk-dysmsapi:1.1.0")
}
