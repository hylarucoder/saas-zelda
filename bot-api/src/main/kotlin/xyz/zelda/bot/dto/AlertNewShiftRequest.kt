package xyz.zelda.bot.dto

import xyz.zelda.company.dto.ShiftDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class AlertNewShiftRequest {
    @NotBlank
    private val userId: String? = null

    @NotNull
    private val newShift: ShiftDto? = null
}