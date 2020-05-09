package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.PermissionService
import xyz.zelda.company.service.ShiftService
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize

@RestController
@RequestMapping("/v1/company/shift")
@Validated
class ShiftController {
    @Autowired
    var shiftService: ShiftService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun createShift(@RequestBody @Validated request: CreateShiftRequest?): GenericShiftResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val shiftDto: ShiftDto? = shiftService!!.createShift(request)
        return GenericShiftResponse(shiftDto)
    }

    @PostMapping(path = "/list_worker_shifts")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_BOT_SERVICE, AuthConstant.AUTHORIZATION_ICAL_SERVICE])
    fun listWorkerShifts(@RequestBody @Validated request: WorkerShiftListRequest?): GenericShiftListResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            // TODO need confirm
            permissionService!!.checkPermissionTeamWorker(request.getCompanyId(), request.getTeamId())
        }
        val shiftList: ShiftList? = shiftService!!.listWorkerShifts(request)
        return GenericShiftListResponse(shiftList)
    }

    @PostMapping(path = "/list_shifts")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listShifts(@RequestBody @Validated request: ShiftListRequest?): GenericShiftListResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(request.getCompanyId(), request.getTeamId())
        }
        val shiftList: ShiftList? = shiftService!!.listShifts(request)
        return GenericShiftListResponse(shiftList)
    }

    @PostMapping(path = "/bulk_publish")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun bulkPublishShifts(@RequestBody @Validated request: BulkPublishShiftsRequest?): GenericShiftListResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(request.getCompanyId(), request.getTeamId())
        }
        val shiftList: ShiftList? = shiftService!!.bulkPublishShifts(request)
        return GenericShiftListResponse(shiftList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun getShift(@RequestParam shiftId: String?, @RequestParam teamId: String?, @RequestParam companyId: String?): GenericShiftResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val shiftDto: ShiftDto? = shiftService!!.getShift(shiftId, teamId, companyId)
        return GenericShiftResponse(shiftDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updateShift(@RequestBody @Validated shiftDto: ShiftDto?): GenericShiftResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(shiftDto.getCompanyId())
        }
        val updatedShiftDto: ShiftDto? = shiftService!!.updateShift(shiftDto)
        return GenericShiftResponse(updatedShiftDto)
    }

    @DeleteMapping(path = "/delete")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun deleteShift(@RequestParam shiftId: String?, @RequestParam teamId: String?, @RequestParam companyId: String?): BaseResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        shiftService!!.deleteShift(shiftId, teamId, companyId)
        return BaseResponse.builder().message("shift deleted").build()
    }
}