package xyz.staffjoy.faraday.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "staffjoy")
class StaffjoyPropreties(val signingSecret: String)
