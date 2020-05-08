package xyz.zelda.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("zelda.infra")
data class ZeldaInfraProperties(
    val sentryDsn: String,
    val deployEnv: String
)
