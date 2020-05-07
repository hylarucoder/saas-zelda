package xyz.staffjoy.common.validation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import kotlin.reflect.KClass

@Documented
@Constraint(validatedBy = [PhoneNumberValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
annotation class PhoneNumber(val message: String = "Invalid phone number", val groups: Array<KClass<*>> = [], val payload: Array<KClass<*>> = []) 