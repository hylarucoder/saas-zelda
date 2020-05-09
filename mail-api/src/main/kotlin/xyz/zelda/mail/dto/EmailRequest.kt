package xyz.zelda.mail.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import lombok.Data
import javax.validation.constraints.NotBlank

@Data
@Builder
class EmailRequest {
    @NotBlank(message = "Please provide an email")
    private val to: String? = null

    @NotBlank(message = "Please provide a subject")
    private val subject: String? = null

    @NotBlank(message = "Please provide a valid body")
    @JsonProperty("html_body")
    private val htmlBody: String? = null
    private val name: String? = null
}