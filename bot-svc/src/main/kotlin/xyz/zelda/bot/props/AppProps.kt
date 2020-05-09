package xyz.zelda.bot.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotNull

@Component
@ConfigurationProperties(prefix = "zelda")
class AppProps {
    private val forceEmailPreference = false
}