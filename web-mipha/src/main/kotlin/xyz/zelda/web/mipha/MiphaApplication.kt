package xyz.zelda.web.mipha

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MiphaApplication

fun main(args: Array<String>) {
    runApplication<MiphaApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
