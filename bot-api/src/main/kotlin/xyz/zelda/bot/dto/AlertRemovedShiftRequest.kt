package xyz.zelda.bot.dto

import xyz.zelda.company.dto.ShiftDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class AlertRemovedShiftRequest {
    @NotBlank
    private val userId: String? = null

    @NotNull
    private val oldShift: ShiftDto? = null
}