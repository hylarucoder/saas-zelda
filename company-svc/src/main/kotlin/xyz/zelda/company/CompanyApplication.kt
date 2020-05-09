package xyz.zelda.company

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

object CompanyApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(CompanyApplication::class.java, args)
    }
}