package xyz.zelda.web.daruk

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import xyz.zelda.web.daruk.config.DarukProperties

@SpringBootApplication
@EnableConfigurationProperties(DarukProperties::class)
open class DarukApplication

fun main(args: Array<String>) {
    runApplication<DarukApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
