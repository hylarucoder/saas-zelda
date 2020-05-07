package xyz.staffjoy.faraday

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
