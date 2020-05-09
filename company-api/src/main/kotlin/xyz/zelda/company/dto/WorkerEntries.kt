package xyz.zelda.company.dto

import java.util.*

class WorkerEntries {
    private val companyId: String? = null
    private val teamId: String? = null

    @Builder.Default
    var workers: List<DirectoryEntryDto> = ArrayList()
}