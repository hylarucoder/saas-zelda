package xyz.zelda.account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class VerifyPasswordRequest {
    @Email
    @NotBlank
    private val email: String? = null

    @NotBlank
    private val password: String? = null
}