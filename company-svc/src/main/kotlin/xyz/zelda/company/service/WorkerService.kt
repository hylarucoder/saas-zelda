package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.company.model.Worker
import xyz.zelda.company.repo.WorkerRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.error.ServiceException

@Service
class WorkerService {
    @Autowired
    var workerRepo: WorkerRepo? = null

    @Autowired
    var teamService: TeamService? = null

    @Autowired
    var directoryService: DirectoryService? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null
    fun listWorkers(companyId: String?, teamId: String?): WorkerEntries? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        val workerList = workerRepo!!.findByTeamId(teamId)
        val workerEntries: WorkerEntries = WorkerEntries.builder().companyId(companyId).teamId(teamId).build()
        for (worker in workerList!!) {
            val directoryEntryDto: DirectoryEntryDto? = directoryService!!.getDirectoryEntry(companyId, worker.getUserId())
            workerEntries.getWorkers().add(directoryEntryDto)
        }
        return workerEntries
    }

    fun getWorker(companyId: String?, teamId: String?, userId: String?): DirectoryEntryDto? {
        // validate and throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        val worker = workerRepo!!.findByTeamIdAndUserId(teamId, userId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "worker relationship not found")
        return directoryService!!.getDirectoryEntry(companyId, userId)
    }

    fun deleteWorker(companyId: String?, teamId: String?, userId: String?) {
        // validate and throw exception if not found
        getWorker(companyId, teamId, userId)
        try {
            workerRepo!!.deleteWorker(teamId, userId)
        } catch (ex: Exception) {
            val errMsg = "failed to delete worker in database"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("worker")
                .targetId(userId)
                .companyId(companyId)
                .teamId(teamId)
                .build()
        logger.info("removed worker", auditLog)
        serviceHelper!!.trackEventAsync("worker_deleted")
    }

    fun getWorkerOf(userId: String?): WorkerOfList? {
        val workerList = workerRepo!!.findByUserId(userId)
        val workerOfList: WorkerOfList = WorkerOfList.builder().userId(userId).build()
        for (worker in workerList!!) {
            val teamDto: TeamDto? = teamService!!.getTeam(worker.getTeamId())
            workerOfList.getTeams().add(teamDto)
        }
        return workerOfList
    }

    fun createWorker(workerDto: WorkerDto?): DirectoryEntryDto? {
        // validate and will throw exception if not found
        teamService!!.getTeamWithCompanyIdValidation(workerDto.getCompanyId(), workerDto.getTeamId())
        val directoryEntryDto: DirectoryEntryDto? = directoryService!!.getDirectoryEntry(workerDto.getCompanyId(), workerDto.getUserId())
        val worker = workerRepo!!.findByTeamIdAndUserId(workerDto.getTeamId(), workerDto.getUserId())
        if (worker != null) {
            throw ServiceException("user is already a worker")
        }
        try {
            val workerToCreate: Worker = builder().teamId(workerDto.getTeamId()).userId(workerDto.getUserId()).build()
            workerRepo.save(workerToCreate)
        } catch (ex: Exception) {
            val errMsg = "failed to create worker in database"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("worker")
                .targetId(workerDto.getUserId())
                .companyId(workerDto.getCompanyId())
                .teamId(workerDto.getTeamId())
                .build()
        logger.info("added worker", auditLog)
        serviceHelper!!.trackEventAsync("worker_created")
        return directoryEntryDto
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(WorkerService::class.java)
    }
}