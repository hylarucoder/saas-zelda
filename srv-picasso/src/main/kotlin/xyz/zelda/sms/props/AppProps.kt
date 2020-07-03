package xyz.zelda.sms.props

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
    // aliyun directmail props
    @NotNull
    private val aliyunAccessKey: String? = null

    @NotNull
    private val aliyunAccessSecret: String? = null

    @NotNull
    private val aliyunSmsSignName: String? = null
    private val whiteListOnly = false
    private val whiteListPhoneNumbers: String? = null
    private val concurrency = 0
}