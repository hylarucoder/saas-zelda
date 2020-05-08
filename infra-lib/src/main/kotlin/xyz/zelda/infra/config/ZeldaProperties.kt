package xyz.zelda.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("zelda")
data class ZeldaProperties(val signingSecret: String)
