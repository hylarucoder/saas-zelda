package xyz.zelda.web.daruk

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DarukApplication

fun main(args: Array<String>) {
    runApplication<DarukApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
