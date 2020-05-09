package xyz.zelda.account.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class VerifyPasswordRequest {
    @Email
    @NotBlank
    private val email: String? = null

    @NotBlank
    private val password: String? = null
}