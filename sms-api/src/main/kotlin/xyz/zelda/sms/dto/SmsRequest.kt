package xyz.zelda.sms.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.NotBlank

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SmsRequest {
    @NotBlank(message = "Please provide a phone number")
    private val to: String? = null

    @NotBlank(message = "Please provide a template code")
    private val templateCode: String? = null
    private val templateParam: String? = null
}