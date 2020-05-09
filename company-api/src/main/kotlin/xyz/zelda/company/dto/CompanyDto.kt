package xyz.zelda.company.dto

import xyz.zelda.infra.validation.DayOfWeek
import xyz.zelda.infra.validation.Group1
import xyz.zelda.infra.validation.Group2
import xyz.zelda.infra.validation.Timezone
import javax.validation.constraints.NotBlank

class CompanyDto {
    @NotBlank(groups = [Group1::class])
    private val id: String? = null

    @NotBlank(groups = [Group1::class, Group2::class])
    private val name: String? = null
    private val archived = false

    @Timezone(groups = [Group1::class, Group2::class])
    @NotBlank(groups = [Group1::class, Group2::class])
    private val defaultTimezone: String? = null

    @DayOfWeek(groups = [Group1::class, Group2::class])
    @NotBlank(groups = [Group1::class, Group2::class])
    private val defaultDayWeekStarts: String? = null
}