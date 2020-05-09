package xyz.zelda.bot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.zelda.bot.service.AlertService
import xyz.zelda.infra.api.BaseResponse

@RestController
@RequestMapping(value = "/v1")
@Validated
class AlertController {
    @Autowired
    private val alertService: AlertService? = null

    @PostMapping(value = "alert_new_shift")
    fun alertNewShift(@RequestBody @Validated request: AlertNewShiftRequest?): BaseResponse {
        alertService!!.alertNewShift(request)
        return BaseResponse.builder().message("new shift alerted").build()
    }

    @PostMapping(value = "alert_new_shifts")
    fun alertNewShifts(@RequestBody @Validated request: AlertNewShiftsRequest?): BaseResponse {
        alertService!!.alertNewShifts(request)
        return BaseResponse.builder().message("new shifts alerted").build()
    }

    @PostMapping(value = "alert_removed_shift")
    fun alertRemovedShift(@RequestBody @Validated request: AlertRemovedShiftRequest?): BaseResponse {
        alertService!!.alertRemovedShift(request)
        return BaseResponse.builder().message("removed shift alerted").build()
    }

    @PostMapping(value = "alert_removed_shifts")
    fun alertRemovedShifts(@RequestBody @Validated request: AlertRemovedShiftsRequest?): BaseResponse {
        alertService!!.alertRemovedShifts(request)
        return BaseResponse.builder().message("removed shifts alerted").build()
    }

    @PostMapping(value = "alert_changed_shifts")
    fun alertChangedShifts(@RequestBody @Validated request: AlertChangedShiftRequest?): BaseResponse {
        alertService!!.alertChangedShift(request)
        return BaseResponse.builder().message("changed shifts alerted").build()
    }
}