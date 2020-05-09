package xyz.zelda.company.dto

import lombok.*
import java.util.*

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class JobList {
    @Builder.Default
    private val jobs: List<JobDto> = ArrayList()
}