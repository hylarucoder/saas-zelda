package xyz.zelda.web.bi

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebCrmApplication

fun main(args: Array<String>) {
    runApplication<WebCrmApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
