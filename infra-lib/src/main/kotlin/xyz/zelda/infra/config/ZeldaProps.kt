package xyz.zelda.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "staffjoy.common")
class ZeldaProps {
    val sentryDsn: @NotBlank String? = null

    // DeployEnvVar is set by Kubernetes during a new deployment so we can identify the code version
    val deployEnv: @NotBlank String? = null
}