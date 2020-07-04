package xyz.zelda.web.daruk.request

import javax.validation.constraints.NotBlank

class LoginRequest {
    var username: @NotBlank String? = null
    var password: @NotBlank String? = null

}