package xyz.zelda.company

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
open class CompanyApplication

fun main(args: Array<String>) {
    runApplication<CompanyApplication>(*args){
        setBannerMode(Banner.Mode.CONSOLE)
    }
}
