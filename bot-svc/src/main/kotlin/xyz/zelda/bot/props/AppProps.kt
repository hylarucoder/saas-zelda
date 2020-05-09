package xyz.zelda.bot.props

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotNull

@Component
@ConfigurationProperties(prefix = "zelda")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AppProps {
    private val forceEmailPreference = false
}