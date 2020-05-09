package xyz.zelda.bot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.zelda.bot.dto.OnboardWorkerRequest
import xyz.zelda.bot.service.OnBoardingService
import xyz.zelda.infra.api.BaseResponse

@RestController
@RequestMapping(value = "/v1")
@Validated
class OnBoardingController {
    @Autowired
    private val onBoardingService: OnBoardingService? = null

    @PostMapping(value = "/onboard_worker")
    fun onboardWorker(@RequestBody @Validated request: OnboardWorkerRequest?): BaseResponse {
        onBoardingService!!.onboardWorker(request)
        return BaseResponse.builder().message("onboarded worker").build()
    }
}