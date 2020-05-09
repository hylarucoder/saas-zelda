package xyz.zelda.account.props

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
    @NotNull
    private val intercomAccessToken: String? = null

    @NotNull
    private val signingSecret: String? = null
}