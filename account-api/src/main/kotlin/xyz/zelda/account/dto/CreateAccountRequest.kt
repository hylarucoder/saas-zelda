package xyz.zelda.account.dto

import org.springframework.util.StringUtils
import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email

class CreateAccountRequest {
    private val name: String? = null

    @Email(message = "Invalid email")
    private val email: String? = null

    @PhoneNumber
    private val phoneNumber: String? = null

    @get:AssertTrue(message = "Empty request")
    private val isValidRequest: Boolean
        private get() = StringUtils.hasText(name) || StringUtils.hasText(email) || StringUtils.hasText(phoneNumber)
}