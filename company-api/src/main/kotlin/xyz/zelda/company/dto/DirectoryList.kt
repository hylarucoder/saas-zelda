package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class DirectoryList {
    @Builder.Default
    private val accounts: List<DirectoryEntryDto> = ArrayList()
    private val limit = 0
    private val offset = 0
}