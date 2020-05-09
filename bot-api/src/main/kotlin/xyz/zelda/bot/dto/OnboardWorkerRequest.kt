package xyz.zelda.bot.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class OnboardWorkerRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val userId: String? = null
}