package xyz.zelda.infra.config

import com.github.structlog4j.StructLog4J
import com.github.structlog4j.json.JsonFormatter
import feign.RequestInterceptor
import io.sentry.Sentry
import io.sentry.SentryClient
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import xyz.zelda.infra.auth.AuthorizeInterceptor
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.auth.FeignRequestHeaderInterceptor
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(ZeldaInfraProperties::class)
open class ZeldaConfig : WebMvcConfigurer {
    @Value("\${spring.profiles.active:NA}")
    lateinit var activeProfile: String

    @Value("\${spring.application.name:NA}")
    lateinit var appName: String

    @Autowired
    lateinit var zeldaInfraProperties: ZeldaInfraProperties

    @Bean
    open fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Bean
    open fun envConfig(): EnvConfig {
        return EnvConfig.getEnvConfig(activeProfile)!!
    }

    @Bean
    open fun sentryClient(): SentryClient {
        val sentryClient: SentryClient = Sentry.init(zeldaInfraProperties.sentryDsn)
        sentryClient.environment = activeProfile
        sentryClient.release = zeldaInfraProperties.deployEnv
        sentryClient.addTag("service", appName)
        return sentryClient
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthorizeInterceptor())
    }

    @Bean
    open fun feignRequestInterceptor(): RequestInterceptor {
        return FeignRequestHeaderInterceptor()
    }

    @PostConstruct
    fun init() {
        // init structured logging
        StructLog4J.setFormatter(JsonFormatter.getInstance())

        // global log fields setting
        StructLog4J.setMandatoryContextSupplier {
            arrayOf<Any?>(
                    "env", activeProfile,
                    "service", appName)
        }
    }

    @PreDestroy
    fun destroy() {
        sentryClient().closeConnection()
    }
}