package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class GrowthGraphResponse {
    private val peopleScheduledPerWeek: Map<String, Int>? = null
    private val peopleOnShift: Int? = null
}