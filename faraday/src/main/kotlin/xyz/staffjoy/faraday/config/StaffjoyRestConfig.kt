package xyz.staffjoy.faraday.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xyz.staffjoy.faraday.aop.SentryClientAspect
import xyz.staffjoy.faraday.exceptions.GlobalExceptionTranslator

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = [StaffjoyConfig::class, SentryClientAspect::class, GlobalExceptionTranslator::class])
open class StaffjoyRestConfig