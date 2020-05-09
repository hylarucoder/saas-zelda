package xyz.zelda.sms

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SmsApplication

fun main(args: Array<String>) {
    runApplication<SmsApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
