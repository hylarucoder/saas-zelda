package xyz.staffjoy.common.config

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
import xyz.staffjoy.common.auth.AuthorizeInterceptor
import xyz.staffjoy.common.auth.FeignRequestHeaderInterceptor
import xyz.staffjoy.common.env.EnvConfig
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(StaffjoyProps::class)
class StaffjoyConfig : WebMvcConfigurer {
    @Value("\${spring.profiles.active:NA}")
    private val activeProfile: String? = null

    @Value("\${spring.application.name:NA}")
    private val appName: String? = null

    @Autowired
    var staffjoyProps: StaffjoyProps? = null

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Bean
    fun envConfig(): EnvConfig {
        return EnvConfig.getEnvConfg(activeProfile)
    }

    @Bean
    fun sentryClient(): SentryClient {
        val sentryClient: SentryClient = Sentry.init(staffjoyProps.getSentryDsn())
        sentryClient.environment = activeProfile
        sentryClient.release = staffjoyProps.getDeployEnv()
        sentryClient.addTag("service", appName)
        return sentryClient
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthorizeInterceptor())
    }

    @Bean
    fun feignRequestInterceptor(): RequestInterceptor {
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