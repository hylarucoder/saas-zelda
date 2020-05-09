plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":infra-lib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
}
