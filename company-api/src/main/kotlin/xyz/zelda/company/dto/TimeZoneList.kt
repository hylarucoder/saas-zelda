package xyz.zelda.company.dto

import lombok.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class TimeZoneList {
    @Singular
    private val timezones: List<String>? = null
}