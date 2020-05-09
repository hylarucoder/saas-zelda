package xyz.zelda.bot.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.company.dto.ShiftDto
import java.util.*
import javax.validation.constraints.NotBlank

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class AlertRemovedShiftsRequest {
    @NotBlank
    private val userId: String? = null

    @Builder.Default
    private val oldShifts: List<ShiftDto> = ArrayList<ShiftDto>()
}