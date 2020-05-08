package xyz.zelda.infra.auth

import kotlin.annotation.Retention
import kotlin.annotation.MustBeDocumented

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Authorize( // allowed consumers
        vararg val value: String) 