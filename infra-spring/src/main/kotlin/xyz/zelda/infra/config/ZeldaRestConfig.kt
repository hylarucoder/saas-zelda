package xyz.zelda.infra.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xyz.zelda.infra.aop.SentryClientAspect
import xyz.zelda.infra.exception.GlobalExceptionTranslator

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = [ZeldaConfig::class, SentryClientAspect::class, GlobalExceptionTranslator::class])
open class ZeldaRestConfig