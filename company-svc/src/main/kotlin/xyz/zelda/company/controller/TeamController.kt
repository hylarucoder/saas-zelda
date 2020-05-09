package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.PermissionService
import xyz.zelda.company.service.TeamService
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize

@RestController
@RequestMapping("/v1/company/team")
@Validated
class TeamController {
    @Autowired
    var teamService: TeamService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun createTeam(@RequestBody @Validated request: CreateTeamRequest?): GenericTeamResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val teamDto: TeamDto? = teamService!!.createTeam(request)
        return GenericTeamResponse(teamDto)
    }

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listTeams(@RequestParam companyId: String?): ListTeamResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyId)
        }
        val teamList: TeamList? = teamService!!.listTeams(companyId)
        return ListTeamResponse(teamList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_BOT_SERVICE, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_ICAL_SERVICE, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE])
    fun getTeam(@RequestParam companyId: String?, @RequestParam teamId: String?): GenericTeamResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionTeamWorker(companyId, teamId)
        }
        val teamDto: TeamDto? = teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        return GenericTeamResponse(teamDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updateTeam(@RequestBody @Validated teamDto: TeamDto?): GenericTeamResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(teamDto.getCompanyId())
        }
        val updatedTeamDto: TeamDto? = teamService!!.updateTeam(teamDto)
        return GenericTeamResponse(updatedTeamDto)
    }

    @GetMapping(path = "/get_worker_team_info")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_ICAL_SERVICE])
    fun getWorkerTeamInfo(@RequestParam(required = false) companyId: String?, @RequestParam userId: String?): GenericWorkerResponse? {
        val response = GenericWorkerResponse()
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            if (userId != AuthContext.getUserId()) { // user can access their own entry
                if (StringUtils.isEmpty(companyId)) {
                    response.setCode(ResultCode.PARAM_MISS)
                    response.setMessage("missing companyId")
                    return response
                }
                permissionService!!.checkPermissionCompanyAdmin(companyId)
            }
        }
        val workerDto: WorkerDto? = teamService!!.getWorkerTeamInfo(userId)
        response.setWorker(workerDto)
        return response
    }
}