package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class AssociationList {
    @Builder.Default
    private val accounts: List<Association> = ArrayList()
    private val limit = 0
    private val offset = 0
}