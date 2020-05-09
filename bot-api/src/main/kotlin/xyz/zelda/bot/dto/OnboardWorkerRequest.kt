package xyz.zelda.bot.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class OnboardWorkerRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val userId: String? = null
}