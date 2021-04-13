plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":sass-core"))
    implementation(project(":sass-starter"))
    implementation(project(":sass-spring"))
}
