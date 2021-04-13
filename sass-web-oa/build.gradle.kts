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
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.21.1")
}
