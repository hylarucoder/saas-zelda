package xyz.staffjoy.common.validation

import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DayOfWeekValidator : ConstraintValidator<DayOfWeek?, String?> {
    private val daysOfWeek = Arrays.asList("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday")
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true // can be null
        val input = value.trim { it <= ' ' }.toLowerCase()
        return if (daysOfWeek.contains(input)) {
            true
        } else false
    }
}