plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":infra-spring"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE"){
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow:2.3.0.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RELEASE")
    testImplementation("junit:junit:4.12")

}
