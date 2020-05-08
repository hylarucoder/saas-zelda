package xyz.zelda.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "staffjoy")
class ZeldaProperties(val signingSecret: String)
