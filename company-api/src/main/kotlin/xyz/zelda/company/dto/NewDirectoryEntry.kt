package xyz.zelda.company.dto

import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class NewDirectoryEntry {
    @NotBlank
    private val companyId: String? = null

    @Builder.Default
    private val name = ""

    @Email
    private val email: String? = null

    @PhoneNumber
    private val phoneNumber: String? = null

    @Builder.Default
    private val internalId = ""
}