import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.32"
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.allopen") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}




allprojects {
    group = "xyz.zelda"
    version = "1.0.0"

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
            url = uri("https://maven.aliyun.com/repository/public")
        }

        maven {
//            url = uri("http://jcenter.bintray.com")
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/spring")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/apache-snapshots")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring-plugin")
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
            url = uri("https://maven.aliyun.com/repository/public")
        }

        maven {
//            url = uri("http://jcenter.bintray.com")
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/spring")
        }


        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/apache-snapshots")
        }

        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring-plugin")
        }

    }
}

