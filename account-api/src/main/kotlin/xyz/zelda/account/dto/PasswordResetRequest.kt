package xyz.zelda.account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class PasswordResetRequest {
    @Email
    @NotEmpty
    private val email: String? = null
}