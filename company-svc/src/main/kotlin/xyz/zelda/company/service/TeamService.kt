package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import xyz.zelda.company.model.Team
import xyz.zelda.company.repo.CompanyRepo
import xyz.zelda.company.repo.TeamRepo
import xyz.zelda.company.repo.WorkerRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.exception.ServiceException

@Service
class TeamService {
    @Autowired
    private val modelMapper: ModelMapper? = null

    @Autowired
    var teamRepo: TeamRepo? = null

    @Autowired
    var companyRepo: CompanyRepo? = null

    @Autowired
    var workerRepo: WorkerRepo? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null
    fun createTeam(request: CreateTeamRequest?): TeamDto? {
        val company = companyRepo!!.findCompanyById(request.getCompanyId())
                ?: throw ServiceException(ResultCode.NOT_FOUND, "Company with specified id not found")

        // sanitize
        if (StringUtils.isEmpty(request.getDayWeekStarts())) {
            request.setDayWeekStarts(company.getDefaultDayWeekStarts())
        }
        if (StringUtils.isEmpty(request.getTimezone())) {
            request.setTimezone(company.getDefaultTimezone())
        }
        val team: Team = builder()
                .companyId(request.getCompanyId())
                .name(request.getName())
                .dayWeekStarts(request.getDayWeekStarts())
                .timezone(request.getTimezone())
                .color(request.getColor())
                .build()
        try {
            teamRepo.save(team)
        } catch (ex: Exception) {
            val errMsg = "could not create team"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("team")
                .targetId(team.getId())
                .companyId(request.getCompanyId())
                .teamId(team.getId())
                .updatedContents(team.toString())
                .build()
        logger.info("created team", auditLog)
        serviceHelper!!.trackEventAsync("team_created")
        return convertToDto(team)
    }

    fun listTeams(companyId: String?): TeamList? {
        val company = companyRepo!!.findCompanyById(companyId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "Company with specified id not found")
        val teams = teamRepo!!.findByCompanyId(companyId)
        val teamList: TeamList = TeamList.builder().build()
        for (team in teams!!) {
            val teamDto: TeamDto? = getTeamWithCompanyIdValidation(team.getCompanyId(), team.getId())
            teamList.getTeams().add(teamDto)
        }
        return teamList
    }

    fun getTeamWithCompanyIdValidation(companyId: String?, teamId: String?): TeamDto? {
        val company = companyRepo!!.findCompanyById(companyId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "Company with specified id not found")
        return getTeam(teamId)
    }

    fun getTeam(teamId: String?): TeamDto? {
        val team: Team = teamRepo.findById(teamId).orElse(null)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "team with specified id not found")
        return convertToDto(team)
    }

    fun updateTeam(teamToUpdate: TeamDto?): TeamDto? {
        val orig: TeamDto? = getTeamWithCompanyIdValidation(teamToUpdate.getCompanyId(), teamToUpdate.getId())
        val team = convertToModel(teamToUpdate)
        try {
            teamRepo.save(team)
        } catch (ex: Exception) {
            val errMsg = "could not update the team"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("team")
                .targetId(orig.getId())
                .companyId(teamToUpdate.getCompanyId())
                .teamId(orig.getId())
                .originalContents(orig.toString())
                .updatedContents(teamToUpdate.toString())
                .build()
        logger.info("updated team", auditLog)
        serviceHelper!!.trackEventAsync("team_updated")
        return teamToUpdate
    }

    // GetWorkerTeamInfo is an internal API method that given a worker UUID will
    // return team and company UUID - it's expected in the future that a
    // worker might belong to multiple teams/companies so this will prob.
    // need to be refactored at some point
    fun getWorkerTeamInfo(userId: String?): WorkerDto? {
        val workers = workerRepo!!.findByUserId(userId)
        if (workers!!.size == 0) {
            throw ServiceException(ResultCode.NOT_FOUND, "worker with specified user id not found")
        }
        val worker = workers[0]
        val team: TeamDto? = getTeam(worker.getTeamId())
        return WorkerDto.builder()
                .teamId(worker.getTeamId())
                .userId(worker.getUserId())
                .companyId(team.getCompanyId())
                .build()
    }

    private fun convertToDto(team: Team?): TeamDto? {
        return modelMapper.map(team, TeamDto::class.java)
    }

    private fun convertToModel(teamDto: TeamDto?): Team? {
        return modelMapper.map(teamDto, Team::class.java)
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(TeamService::class.java)
    }
}