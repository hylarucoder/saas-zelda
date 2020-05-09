package xyz.zelda.account.dto

import javax.validation.constraints.NotBlank

class SyncUserRequest {
    @NotBlank
    private val userId: String? = null
}