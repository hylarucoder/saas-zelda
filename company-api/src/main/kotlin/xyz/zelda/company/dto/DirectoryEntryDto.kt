package xyz.zelda.company.dto

import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

// directory
class DirectoryEntryDto {
    @NotBlank
    private val userId: String? = null

    @NotBlank
    private val internalId: String? = null

    @NotBlank
    private val companyId: String? = null

    // coming from account
    @NotBlank
    @Builder.Default
    private val name = ""

    @NotBlank
    @Email
    private val email: String? = null
    private val confirmedAndActive = false

    @NotBlank
    @PhoneNumber
    private val phoneNumber: String? = null
    private val photoUrl: String? = null
}