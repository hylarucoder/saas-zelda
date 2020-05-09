package xyz.zelda.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import xyz.zelda.infra.async.ContextCopyingDecorator
import xyz.zelda.infra.config.ZeldaRestConfig
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@Import(value = [ZeldaRestConfig::class])
open class AppConfig {
    @Bean(name = [ASYNC_EXECUTOR_NAME])
    open fun asyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.setTaskDecorator(ContextCopyingDecorator())
        executor.corePoolSize = 3
        executor.maxPoolSize = 5
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