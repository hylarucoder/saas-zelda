package xyz.zelda.whoami.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class IntercomSettingsDto {
    private val appId: String? = null
    private val userId: String? = null
    private val userHash: String? = null
    private val name: String? = null
    private val email: String? = null
    private val createdAt: Instant? = null
}