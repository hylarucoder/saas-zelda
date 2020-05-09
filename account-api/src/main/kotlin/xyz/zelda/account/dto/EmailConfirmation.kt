package xyz.zelda.account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class EmailConfirmation {
    @NotBlank
    private val userId: String? = null

    @NotEmpty
    @Email
    private val email: String? = null
}