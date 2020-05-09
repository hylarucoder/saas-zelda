package xyz.zelda.company.dto

import lombok.*
import java.time.Instant
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ShiftList {
    @Builder.Default
    private val shifts: List<ShiftDto> = ArrayList()
    private val shiftStartAfter: Instant? = null
    private val shiftStartBefore: Instant? = null
}