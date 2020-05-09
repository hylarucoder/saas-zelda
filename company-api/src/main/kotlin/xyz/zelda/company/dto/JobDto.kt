package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class JobDto {
    @NotBlank
    private val id: String? = null

    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val name: String? = null
    private val archived = false

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @NotBlank
    private val color: String? = null
}