package xyz.zelda.spring.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xyz.zelda.spring.aop.SentryClientAspect

/**
 * Use this common config for Web App
 */
@Configuration
@Import(value = [ZeldaConfig::class, SentryClientAspect::class])
open class ZeldaWebConfig