package xyz.staffjoy.common.aop

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import xyz.staffjoy.common.env.EnvConfig

@Aspect
@Slf4j
class SentryClientAspect {
    @Autowired
    var envConfig: EnvConfig? = null

    @Around("execution(* io.sentry.SentryClient.send*(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint) {
        // no sentry logging in debug mode
        if (envConfig.isDebug()) {
            log.debug("no sentry logging in debug mode")
            return
        }
        joinPoint.proceed()
    }
}