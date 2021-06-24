plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":saas-core"))
    implementation(project(":saas-starter"))
    implementation(project(":saas-spring"))
}
