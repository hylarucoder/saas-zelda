package xyz.zelda.mail.config

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.IAcsClient
import com.aliyuncs.profile.DefaultProfile
import com.aliyuncs.profile.IClientProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import xyz.zelda.infra.config.StaffjoyRestConfig
import xyz.zelda.mail.MailConstant
import xyz.zelda.mail.props.AppProps
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@Import(value = StaffjoyRestConfig::class)
class AppConfig {
    @Autowired
    var appProps: AppProps? = null

    @Bean
    fun acsClient(): IAcsClient {
        val profile: IClientProfile = DefaultProfile.getProfile(MailConstant.ALIYUN_REGION_ID,
                appProps.getAliyunAccessKey(), appProps.getAliyunAccessSecret())
        return DefaultAcsClient(profile)
    }

    @Bean(name = ASYNC_EXECUTOR_NAME)
    fun asyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.setCorePoolSize(3)
        executor.setMaxPoolSize(5)
        executor.setQueueCapacity(100)
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setThreadNamePrefix("AsyncThread-")
        executor.initialize()
        return executor
    }

    companion object {
        const val ASYNC_EXECUTOR_NAME = "asyncExecutor"
    }
}