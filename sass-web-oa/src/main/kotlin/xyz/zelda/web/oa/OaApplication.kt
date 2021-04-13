package xyz.zelda.web.oa

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OaApplication

fun main(args: Array<String>) {
    runApplication<OaApplication>(*args) {
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
