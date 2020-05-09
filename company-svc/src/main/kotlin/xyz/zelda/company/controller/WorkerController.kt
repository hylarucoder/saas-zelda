package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.PermissionService
import xyz.zelda.company.service.WorkerService
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize

@RestController
@RequestMapping("/v1/company/worker")
@Validated
class WorkerController {
    @Autowired
    var workerService: WorkerService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listWorkers(@RequestParam companyId: String?, @RequestParam teamId: String?): ListWorkerResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val workerEntries: WorkerEntries? = workerService!!.listWorkers(companyId, teamId)
        return ListWorkerResponse(workerEntries)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun getWorker(@RequestParam companyId: String?, @RequestParam teamId: String?, @RequestParam userId: String?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val directoryEntryDto: DirectoryEntryDto? = workerService!!.getWorker(companyId, teamId, userId)
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @DeleteMapping(path = "/delete")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun deleteWorker(@RequestBody @Validated workerDto: WorkerDto?): BaseResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(workerDto.getCompanyId())
        }
        workerService!!.deleteWorker(workerDto.getCompanyId(), workerDto.getTeamId(), workerDto.getUserId())
        return BaseResponse.builder().message("worker has been deleted").build()
    }

    @GetMapping(path = "/get_worker_of")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_WWW_SERVICE,  // This is an internal endpoint
        AuthConstant.AUTHORIZATION_WHOAMI_SERVICE])
    fun getWorkerOf(@RequestParam userId: String?): GetWorkerOfResponse? {
        val workerOfList: WorkerOfList? = workerService!!.getWorkerOf(userId)
        return GetWorkerOfResponse(workerOfList)
    }

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE])
    fun createWorker(@RequestBody @Validated workerDto: WorkerDto?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(workerDto.getCompanyId())
        }
        val directoryEntryDto: DirectoryEntryDto? = workerService!!.createWorker(workerDto)
        return GenericDirectoryResponse(directoryEntryDto)
    }
}