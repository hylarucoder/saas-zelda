package xyz.zelda.company.dto

import java.util.*

class AdminEntries {
    private val companyId: String? = null

    @Builder.Default
    private val admins: List<DirectoryEntryDto> = ArrayList()
}