plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    api(project(":sass-core"))
    api(project(":sass-starter"))
    api(project(":sass-spring"))

//    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.21.1")
//    implementation("org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE")
//    implementation("org.zalando:problem-spring-web:0.24.0")
}
