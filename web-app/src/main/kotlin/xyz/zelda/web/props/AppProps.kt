package xyz.zelda.web.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotNull

@Component
@ConfigurationProperties(prefix = "zelda")
class AppProps {
    lateinit var recaptchaPublic: String

    @NotNull
    lateinit var signingSecret: String
}