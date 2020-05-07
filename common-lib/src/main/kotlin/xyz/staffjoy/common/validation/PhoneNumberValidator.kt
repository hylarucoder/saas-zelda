package xyz.staffjoy.common.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PhoneNumberValidator : ConstraintValidator<PhoneNumber?, String?> {
    override fun isValid(phoneField: String?, context: ConstraintValidatorContext): Boolean {
        return if (phoneField == null) true else phoneField.matches("[0-9]+") && phoneField.length > 8 && phoneField.length < 14 // can be null
    }
}