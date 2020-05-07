  
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
    val kotlinVersion = "1.3.70"
    id("org.springframework.boot") version "2.2.0.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
}

allprojects {
    group = "com.kotlin"
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
