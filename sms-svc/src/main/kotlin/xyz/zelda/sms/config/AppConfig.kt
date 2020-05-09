package xyz.zelda.sms.config

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.IAcsClient
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.profile.DefaultProfile
import com.aliyuncs.profile.IClientProfile
import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import xyz.zelda.infra.config.StaffjoyRestConfig
import xyz.zelda.sms.SmsConstant
import xyz.zelda.sms.props.AppProps
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@Import(value = StaffjoyRestConfig::class)
class AppConfig {
    @Autowired
    var appProps: AppProps? = null

    @Bean
    fun acsClient(@Autowired sentryClient: SentryClient): IAcsClient {
        val profile: IClientProfile = DefaultProfile.getProfile(SmsConstant.ALIYUN_REGION_ID, appProps.getAliyunAccessKey(), appProps.getAliyunAccessSecret())
        try {
            DefaultProfile.addEndpoint(SmsConstant.ALIYUN_SMS_ENDPOINT_NAME, SmsConstant.ALIYUN_REGION_ID, SmsConstant.ALIYUN_SMS_PRODUCT, SmsConstant.ALIYUN_SMS_DOMAIN)
        } catch (ex: ClientException) {
            sentryClient.sendException(ex)
            logger.error("Fail to create acsClient ", ex)
        }
        return DefaultAcsClient(profile)
    }

    @Bean(name = ASYNC_EXECUTOR_NAME)
    fun asyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.setCorePoolSize(appProps.getConcurrency())
        executor.setMaxPoolSize(appProps.getConcurrency())
        executor.setQueueCapacity(SmsConstant.DEFAULT_EXECUTOR_QUEUE_CAPACITY)
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setThreadNamePrefix("AsyncThread-")
        executor.initialize()
        return executor
    }

    companion object {
        const val ASYNC_EXECUTOR_NAME = "asyncExecutor"
        private val logger: ILogger = SLoggerFactory.getLogger(AppConfig::class.java)
    }
}