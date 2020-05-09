package xyz.zelda.bot.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.validation.constraints.NotBlank

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class GreetingRequest {
    @NotBlank
    private val userId: String? = null
}