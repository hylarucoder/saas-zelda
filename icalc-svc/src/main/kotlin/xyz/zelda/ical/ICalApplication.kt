package xyz.zelda.ical

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.Banner
import org.springframework.boot.runApplication


@SpringBootApplication
open class ICalApplication

fun main(args: Array<String>) {
    runApplication<ICalApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
