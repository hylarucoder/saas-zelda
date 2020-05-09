package xyz.zelda.web.config

import ch.mfrey.thymeleaf.extras.with.WithDialect
import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ViewConfig {
    @Bean
    fun layoutDialect(): LayoutDialect {
        return LayoutDialect()
    }

    @Bean
    fun withDialect(): WithDialect {
        return WithDialect()
    }
}