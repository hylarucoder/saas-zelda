package xyz.zelda.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "zelda")
class ZeldaProperties(val signingSecret: String)
