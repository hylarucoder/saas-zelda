package xyz.zelda.bot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xyz.zelda.bot.BotConstant
import xyz.zelda.bot.dto.*
import xyz.zelda.infra.api.BaseResponse

@FeignClient(name = BotConstant.SERVICE_NAME, path = "/v1", url = "\${zelda.bot-service-endpoint}")
interface BotClient {
    @PostMapping(path = "sms_greeting")
    fun sendSmsGreeting(@RequestBody @Validated request: GreetingRequest?): BaseResponse?

    @PostMapping(path = "onboard_worker")
    fun onboardWorker(@RequestBody @Validated request: OnboardWorkerRequest?): BaseResponse?

    @PostMapping(path = "alert_new_shift")
    fun alertNewShift(@RequestBody @Validated request: AlertNewShiftRequest?): BaseResponse?

    @PostMapping(path = "alert_new_shifts")
    fun alertNewShifts(@RequestBody @Validated request: AlertNewShiftsRequest?): BaseResponse?

    @PostMapping(path = "alert_removed_shift")
    fun alertRemovedShift(@RequestBody @Validated request: AlertRemovedShiftRequest?): BaseResponse?

    @PostMapping(path = "alert_removed_shifts")
    fun alertRemovedShifts(@RequestBody @Validated request: AlertRemovedShiftsRequest?): BaseResponse?

    @PostMapping(path = "alert_changed_shifts")
    fun alertChangedShift(@RequestBody @Validated request: AlertChangedShiftRequest?): BaseResponse?
}