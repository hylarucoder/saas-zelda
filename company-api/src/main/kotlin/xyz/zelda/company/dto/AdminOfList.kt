package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AdminOfList {
    private val userId: String? = null

    @Builder.Default
    private val companies: List<CompanyDto> = ArrayList()
}