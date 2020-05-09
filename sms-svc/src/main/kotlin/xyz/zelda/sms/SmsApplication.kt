package xyz.zelda.sms

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

object SmsApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(SmsApplication::class.java, args)
    }
}