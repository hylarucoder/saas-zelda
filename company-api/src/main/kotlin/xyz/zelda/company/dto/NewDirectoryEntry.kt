package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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