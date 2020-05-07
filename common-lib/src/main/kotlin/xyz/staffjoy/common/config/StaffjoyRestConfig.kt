package xyz.staffjoy.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xyz.staffjoy.common.aop.SentryClientAspect
import xyz.staffjoy.common.error.GlobalExceptionTranslator

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = [StaffjoyConfig::class, SentryClientAspect::class, GlobalExceptionTranslator::class])
class StaffjoyRestConfig 