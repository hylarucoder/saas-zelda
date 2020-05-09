package xyz.zelda.account.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class UpdatePasswordRequest {
    @NotBlank
    private val userId: String? = null

    @NotBlank
    @Size(min = 6)
    private val password: String? = null
}