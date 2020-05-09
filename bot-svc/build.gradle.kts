plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":infra-lib"))
    implementation(project(":bot-api"))
    implementation(project(":account-api"))
    implementation(project(":company-api"))
    implementation(project(":sms-api"))
    implementation(project(":mail-api"))

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.2.0.RELEASE")

}
