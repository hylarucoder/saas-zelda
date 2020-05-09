package xyz.zelda.company.dto

import java.util.*

class WorkerOfList {
    private val userId: String? = null

    @Builder.Default
    private val teams: List<TeamDto> = ArrayList()
}