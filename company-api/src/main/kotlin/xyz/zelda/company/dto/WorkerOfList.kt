package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class WorkerOfList {
    private val userId: String? = null

    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
}