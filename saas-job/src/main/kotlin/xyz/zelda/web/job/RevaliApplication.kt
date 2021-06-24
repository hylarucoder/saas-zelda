package xyz.zelda.web.revali

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RevaliApplication

fun main(args: Array<String>) {
    runApplication<RevaliApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
