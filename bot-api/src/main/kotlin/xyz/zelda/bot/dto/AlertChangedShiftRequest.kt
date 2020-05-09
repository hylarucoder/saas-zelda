package xyz.zelda.bot.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.company.dto.ShiftDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class AlertChangedShiftRequest {
    @NotBlank
    private val userId: String? = null

    @NotNull
    private val oldShift: ShiftDto? = null

    @NotNull
    private val newShift: ShiftDto? = null
}