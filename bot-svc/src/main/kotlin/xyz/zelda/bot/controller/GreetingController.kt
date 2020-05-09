package xyz.zelda.bot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.zelda.bot.dto.GreetingRequest
import xyz.zelda.bot.service.GreetingService
import xyz.zelda.infra.api.BaseResponse

@RestController
@RequestMapping(value = "/v1")
@Validated
class GreetingController {
    @Autowired
    private val greetingService: GreetingService? = null

    @PostMapping(value = "/sms_greeting")
    fun sendSmsGreeting(@RequestBody @Validated request: GreetingRequest): BaseResponse {
        greetingService!!.greeting(request.getUserId())
        return BaseResponse.builder().message("greeting sent").build()
    }
}