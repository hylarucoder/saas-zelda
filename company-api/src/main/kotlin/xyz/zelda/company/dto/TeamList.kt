package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class TeamList {
    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
}