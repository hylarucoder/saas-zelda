package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class WorkerEntries {
    private val companyId: String? = null
    private val teamId: String? = null

    @Builder.Default
    var workers: List<DirectoryEntryDto> = ArrayList()
}