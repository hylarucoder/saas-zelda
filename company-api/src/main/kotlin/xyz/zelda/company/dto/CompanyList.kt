package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CompanyList {
    private val companies: List<CompanyDto>? = null
    private val limit = 0
    private val offset = 0
}