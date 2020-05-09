package xyz.zelda.bot

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
