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
import xyz.staffjoy.common.AuthorizeInterceptor
import xyz.staffjoy.common.EnvConfig
import xyz.staffjoy.common.FeignRequestHeaderInterceptor
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(StaffjoyProps::class)
open class StaffjoyConfig : WebMvcConfigurer {
    @Value("\${spring.profiles.active:NA}")
    private val activeProfile: String? = null

    @Value("\${spring.application.name:NA}")
    private val appName: String? = null

    @Autowired
    var staffjoyProps: StaffjoyProps? = null

    @Bean
    open fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Bean
    open fun envConfig(): EnvConfig {
        return EnvConfig.getEnvConfig(activeProfile)
    }

    @Bean
    open fun sentryClient(): SentryClient {
        val sentryClient: SentryClient = Sentry.init(staffjoyProps.sentryDsn)
        sentryClient.environment = activeProfile
        sentryClient.release = staffjoyProps.deployEnv
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