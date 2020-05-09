package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class AdminEntries {
    private val companyId: String? = null

    @Builder.Default
    private val admins: List<DirectoryEntryDto> = ArrayList()
}