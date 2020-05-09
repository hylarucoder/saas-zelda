package xyz.zelda.mail

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

object MailApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(MailApplication::class.java, args)
    }
}