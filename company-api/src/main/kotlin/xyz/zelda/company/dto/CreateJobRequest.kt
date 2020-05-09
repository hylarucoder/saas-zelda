package xyz.zelda.company.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class CreateJobRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val name: String? = null

    @NotBlank
    private val color: String? = null
}