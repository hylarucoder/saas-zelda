package xyz.zelda.bot.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.company.dto.ShiftDto
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class AlertNewShiftsRequest {
    @NotBlank
    private val userId: String? = null

    @NotNull
    @Builder.Default
    private val newShifts: List<ShiftDto> = ArrayList<ShiftDto>()
}