package xyz.zelda.web.daruk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableConfigurationProperties(
        SecurityProperties::class
)
class AppConfiguration(
        securityProperties: SecurityProperties
) {
    @Bean
    fun bCryptPasswordEncoder():BCryptPasswordEncoder {
        return BCryptPasswordEncoder(securityProperties.strength)
    }

}