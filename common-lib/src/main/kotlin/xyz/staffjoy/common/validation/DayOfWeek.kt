package xyz.staffjoy.common.validation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import kotlin.reflect.KClass

@Documented
@Constraint(validatedBy = [DayOfWeekValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class DayOfWeek(val message: String = "Unknown day of week", val groups: Array<KClass<*>> = [], val payload: Array<KClass<*>> = []) 