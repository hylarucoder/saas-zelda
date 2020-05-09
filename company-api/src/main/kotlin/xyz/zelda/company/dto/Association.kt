package xyz.zelda.company.dto

import java.util.*

class Association {
    private val account: DirectoryEntryDto? = null

    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
    private val admin: Boolean? = null
}