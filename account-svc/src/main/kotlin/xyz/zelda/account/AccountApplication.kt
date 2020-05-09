package xyz.zelda.account

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

object AccountApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(AccountApplication::class.java, args)
    }
}