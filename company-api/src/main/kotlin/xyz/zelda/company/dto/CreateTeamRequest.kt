package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.infra.validation.DayOfWeek
import xyz.zelda.infra.validation.Timezone
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class CreateTeamRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val name: String? = null

    @Timezone
    private val timezone: String? = null

    @DayOfWeek
    private val dayWeekStarts: String? = null

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @NotEmpty
    private val color: String? = null
}