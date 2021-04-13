package xyz.zelda.spring.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import xyz.zelda.spring.env.EnvConfig
import xyz.zelda.spring.utils.loggerFor

@Aspect
class SentryClientAspect {
    companion object {
        val logger = loggerFor()
    }
    @Autowired
    lateinit var envConfig: EnvConfig

    @Around("execution(* io.sentry.SentryClient.send*(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint) {
        // no sentry logging in debug mode
        logger.debug("no sentry logging in debug mode")
        if (envConfig.debug) {
            logger.debug("no sentry logging in debug mode")
            return
        }
        joinPoint.proceed()
    }
}