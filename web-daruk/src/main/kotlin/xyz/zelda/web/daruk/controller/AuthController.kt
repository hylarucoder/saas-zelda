package xyz.zelda.web.daruk.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.zelda.web.daruk.request.LoginRequest
import xyz.zelda.web.daruk.request.SignupRequest
import xyz.zelda.web.daruk.response.MessageResponse
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/account")
class AuthController {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: @Valid LoginRequest?): ResponseEntity<*> {
        return ResponseEntity.ok<Any>(MessageResponse("wocao"))
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest?): ResponseEntity<*> {
        return ResponseEntity.ok<Any>(MessageResponse("User registered successfully!"))
    }
}