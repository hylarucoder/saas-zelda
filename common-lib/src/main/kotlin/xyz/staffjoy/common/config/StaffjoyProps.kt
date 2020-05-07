package xyz.staffjoy.common.config

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "staffjoy.common")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class StaffjoyProps {
    private val sentryDsn: @NotBlank String? = null

    // DeployEnvVar is set by Kubernetes during a new deployment so we can identify the code version
    private val deployEnv: @NotBlank String? = null
}