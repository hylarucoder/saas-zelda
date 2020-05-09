package xyz.zelda.company.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class WorkerDto {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val userId: String? = null
}