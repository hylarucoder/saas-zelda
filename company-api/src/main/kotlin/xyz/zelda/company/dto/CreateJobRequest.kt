package xyz.zelda.company.dto

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
class CreateJobRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val name: String? = null

    @NotBlank
    private val color: String? = null
}