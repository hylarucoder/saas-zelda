import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Libraries {
    object Threading {
        object Versions {
            const val kotlin = "1.3.72"
            const val springBoot = "2.3.0.RELEASE"
        }
        const val kotlinJson = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0"
        const val cloudOpenfeign = "org.springframework.cloud:spring-cloud-starter-openfeign:2.1.0.RELEASE"

        object Spring {
            const val actuator = "org.springframework.boot:spring-boot-starter-actuator:2.2.0.RELEASE"
            const val web = "org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE"
            const val starter = "org.springframework.boot:spring-boot-starter:2.2.0.RELEASE"
        }
    }
}

plugins {
    val kotlinVersion = "1.3.72"
    id("org.springframework.boot") version "2.3.0.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.allopen") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}


allprojects {
    group = "xyz.zelda"
    version = "1.0.0"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
            incremental = false
        }
    }

    repositories {
        mavenLocal()
        maven {
            url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
        }

        maven {
            url = uri("http://jcenter.bintray.com")
        }

        maven {
            url = uri("http://repo.maven.apache.org/maven2")
        }

    }
}

subprojects {
    apply {
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
    }

    repositories {
        mavenLocal()
        maven {
            url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
        }

        maven {
            url = uri("http://jcenter.bintray.com")
        }

        maven {
            url = uri("http://repo.maven.apache.org/maven2")
        }

    }
}

