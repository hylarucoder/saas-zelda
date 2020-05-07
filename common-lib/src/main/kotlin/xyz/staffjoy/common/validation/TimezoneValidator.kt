package xyz.staffjoy.common.validation

import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TimezoneValidator : ConstraintValidator<Timezone?, String?> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) true else listOf(*TimeZone.getAvailableIDs()).contains(value)
    }
}