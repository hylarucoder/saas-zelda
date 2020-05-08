package xyz.zelda.infra.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import xyz.zelda.infra.utils.logger

@Aspect
class SentryClientAspect {
    private val log = logger()
//    @Autowired
//    var envConfig: EnvConfig? = null

    @Around("execution(* io.sentry.SentryClient.send*(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint) {
        // no sentry logging in debug mode
        log.debug("no sentry logging in debug mode")
//        if (envConfig!!.debug) {
//            log.debug("no sentry logging in debug mode")
//            return
//        }
        joinPoint.proceed()
    }
}