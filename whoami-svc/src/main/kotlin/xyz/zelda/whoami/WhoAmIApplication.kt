package xyz.zelda.whoami

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import
import xyz.zelda.infra.config.StaffjoyRestConfig

object WhoAmIApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(WhoAmIApplication::class.java, args)
    }
}