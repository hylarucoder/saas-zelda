package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Association {
    private val account: DirectoryEntryDto? = null

    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
    private val admin: Boolean? = null
}