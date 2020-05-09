package xyz.zelda.account.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UpdatePasswordRequest {
    @NotBlank
    private val userId: String? = null

    @NotBlank
    @Size(min = 6)
    private val password: String? = null
}