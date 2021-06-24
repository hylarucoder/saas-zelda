package xyz.zelda.gateway

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FaradayApplication

fun main(args: Array<String>) {
    runApplication<FaradayApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
