plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(project(":infra-lib"))
    implementation(project(":account-api"))
    implementation(project(":company-api"))
    implementation(project(":mail-api"))
    implementation(project(":bot-api"))

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.2.0.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.1.2.RELEASE")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.intercom:intercom-java:2.8.0")

    runtimeOnly("mysql:mysql-connector-java:8.0.13")
    testImplementation("com.h2database:h2:1.4.197")

}
