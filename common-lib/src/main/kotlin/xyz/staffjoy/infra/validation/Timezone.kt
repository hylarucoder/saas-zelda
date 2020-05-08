package xyz.staffjoy.common.validation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import kotlin.reflect.KClass

@Documented
@Constraint(validatedBy = [TimezoneValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class Timezone(val message: String = "Invalid timezone", val groups: Array<KClass<*>> = [], val payload: Array<KClass<*>> = []) 