package xyz.zelda.account.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "zelda")
open class AppProps(
        private val intercomAccessToken: String,
        private val signingSecret: String
)
