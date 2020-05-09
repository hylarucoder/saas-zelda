package xyz.zelda.account.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.NotBlank

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class TrackEventRequest {
    @NotBlank
    private val userId: String? = null

    @NotBlank
    private val event: String? = null
}