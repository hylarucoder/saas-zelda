package xyz.zelda.account.dto

import javax.validation.constraints.NotBlank

class TrackEventRequest {
    @NotBlank
    private val userId: String? = null

    @NotBlank
    private val event: String? = null
}