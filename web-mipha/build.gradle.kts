plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api(project(":infra-starters"))
    api(project(":infra-common"))
    api(project(":infra-spring"))
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.21.1")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE")
    implementation("org.zalando:problem-spring-web:0.24.0")
}
