package xyz.zelda.company.dto

import xyz.zelda.infra.validation.DayOfWeek
import xyz.zelda.infra.validation.Timezone
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class TeamDto {
    @NotBlank
    private val id: String? = null

    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val name: String? = null
    private val archived = false

    @Timezone
    @NotBlank
    private val timezone: String? = null

    @DayOfWeek
    @NotBlank
    private val dayWeekStarts: String? = null

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @NotBlank
    private val color: String? = null
}