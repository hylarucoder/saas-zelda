package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.JobService
import xyz.zelda.company.service.PermissionService
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize

@RestController
@RequestMapping("/v1/company/job")
@Validated
class JobController {
    @Autowired
    var jobService: JobService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun createJob(@RequestBody @Validated request: CreateJobRequest?): GenericJobResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val jobDto: JobDto? = jobService!!.createJob(request)
        return GenericJobResponse(jobDto)
    }

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listJobs(@RequestParam companyId: String?, @RequestParam teamId: String?): ListJobResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) { // TODO need confirm
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val jobList: JobList? = jobService!!.listJobs(companyId, teamId)
        return ListJobResponse(jobList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_BOT_SERVICE])
    fun getJob(jobId: String?, companyId: String?, teamId: String?): GenericJobResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val jobDto: JobDto? = jobService!!.getJob(jobId, companyId, teamId)
        return GenericJobResponse(jobDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updateJob(@RequestBody @Validated jobDto: JobDto?): GenericJobResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(jobDto.getCompanyId())
        }
        val updatedJobDto: JobDto? = jobService!!.updateJob(jobDto)
        return GenericJobResponse(updatedJobDto)
    }
}