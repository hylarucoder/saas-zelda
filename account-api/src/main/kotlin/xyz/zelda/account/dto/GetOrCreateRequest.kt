package xyz.zelda.account.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.util.StringUtils
import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class GetOrCreateRequest {
    private val name: String? = null

    @Email(message = "Invalid email")
    private val email: String? = null

    @PhoneNumber
    private val phoneNumber: String? = null

    @get:AssertTrue(message = "Empty request")
    private val isValidRequest: Boolean
        private get() = StringUtils.hasText(name) || StringUtils.hasText(email) || StringUtils.hasText(phoneNumber)
}