package xyz.zelda.company.dto

import java.util.*

class TeamList {
    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
}