package xyz.zelda.ical

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import
import xyz.zelda.infra.config.StaffjoyWebConfig

object ICalApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(ICalApplication::class.java, args)
    }
}