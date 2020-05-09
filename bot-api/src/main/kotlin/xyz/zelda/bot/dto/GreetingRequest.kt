package xyz.zelda.bot.dto

import javax.validation.constraints.NotBlank

class GreetingRequest {
    @NotBlank
    private val userId: String? = null
}