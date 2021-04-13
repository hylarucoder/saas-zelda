plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":sass-core"))

    api("org.springframework.boot:spring-boot-loader-tools:2.4.3")
    api("org.springframework.boot:spring-boot-starter-actuator:2.4.3")
    api("org.springframework.boot:spring-boot-starter-aop:2.4.3")
    api("org.springframework.boot:spring-boot-starter-integration:2.4.3")
    api("org.springframework.boot:spring-boot-starter-jetty:2.4.3")
    api("org.springframework.boot:spring-boot-starter-logging:2.4.3")
    api("org.springframework.boot:spring-boot-starter-mail:2.4.3")
    api("org.springframework.boot:spring-boot-starter-mustache:2.4.3")
    api("org.springframework.boot:spring-boot-starter-thymeleaf:2.4.3")
    api("org.springframework.boot:spring-boot-starter-validation:2.4.3")
    api("org.springframework.boot:spring-boot-starter-web:2.4.3")
    api("org.springframework.boot:spring-boot-starter:2.4.3")
    api("org.springframework.boot:spring-boot-starter-web:2.4.3") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    runtimeOnly("com.h2database:h2:1.3.176")

    api("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.2")


    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.4.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.4.3")
}
