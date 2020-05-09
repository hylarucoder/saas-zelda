package xyz.zelda.whoami

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.Banner
import org.springframework.boot.runApplication

@SpringBootApplication
open class WhoAmIApplication

fun main(args: Array<String>) {
    runApplication<WhoAmIApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
