package xyz.zelda.account.dto

import xyz.zelda.infra.validation.PhoneNumber
import java.time.Instant
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class AccountDto {
    @NotBlank
    private val id: String? = null
    private val name: String? = null

    @Email(message = "Invalid email")
    private val email: String? = null
    private val confirmedAndActive = false

    @NotNull
    private val memberSince: Instant? = null
    private val support = false

    @PhoneNumber
    private val phoneNumber: String? = null

    @NotEmpty
    private val photoUrl: String? = null
}