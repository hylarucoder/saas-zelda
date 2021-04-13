package xyz.zelda.web.urbosa

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkApplication

fun main(args: Array<String>) {
    runApplication<WorkApplication>(*args) {
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
