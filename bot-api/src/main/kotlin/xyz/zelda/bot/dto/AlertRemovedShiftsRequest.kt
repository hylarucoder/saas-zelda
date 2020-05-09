package xyz.zelda.bot.dto

import xyz.zelda.company.dto.ShiftDto
import java.util.*
import javax.validation.constraints.NotBlank

class AlertRemovedShiftsRequest {
    @NotBlank
    private val userId: String? = null

    @Builder.Default
    private val oldShifts: List<ShiftDto> = ArrayList<ShiftDto>()
}