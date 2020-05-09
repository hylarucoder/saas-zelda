package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.company.dto.CreateJobRequest
import xyz.zelda.company.dto.JobDto
import xyz.zelda.company.dto.JobList
import xyz.zelda.company.model.Job
import xyz.zelda.company.repo.JobRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.error.ServiceException

@Service
class JobService {
    @Autowired
    var jobRepo: JobRepo? = null

    @Autowired
    var teamService: TeamService? = null

    @Autowired
    private val modelMapper: ModelMapper? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null
    fun createJob(request: CreateJobRequest?): JobDto? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(request.getCompanyId(), request.getTeamId())
        val job: Job = builder()
                .name(request.getName())
                .color(request.getColor())
                .teamId(request.getTeamId())
                .build()
        try {
            jobRepo.save(job)
        } catch (ex: Exception) {
            val errMsg = "could not create job"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("job")
                .targetId(job.getId())
                .companyId(request.getCompanyId())
                .teamId(job.getTeamId())
                .updatedContents(job.toString())
                .build()
        logger.info("created job", auditLog)
        serviceHelper!!.trackEventAsync("job_created")
        val jobDto: JobDto? = convertToDto(job)
        jobDto.setCompanyId(request.getCompanyId())
        return jobDto
    }

    fun listJobs(companyId: String?, teamId: String?): JobList? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        val jobList: JobList = JobList.builder().build()
        val jobs = jobRepo!!.findJobByTeamId(teamId)
        for (job in jobs!!) {
            val jobDto: JobDto? = convertToDto(job)
            jobDto.setCompanyId(companyId)
            jobList.getJobs().add(jobDto)
        }
        return jobList
    }

    fun getJob(jobId: String?, companyId: String?, teamId: String?): JobDto? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        val job = jobRepo!!.findJobById(jobId) ?: throw ServiceException(ResultCode.NOT_FOUND, "job not found")
        val jobDto: JobDto? = convertToDto(job)
        jobDto.setCompanyId(companyId)
        return jobDto
    }

    fun updateJob(jobDtoToUpdate: JobDto?): JobDto? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(jobDtoToUpdate.getCompanyId(), jobDtoToUpdate.getTeamId())
        val orig: JobDto? = getJob(jobDtoToUpdate.getId(), jobDtoToUpdate.getCompanyId(), jobDtoToUpdate.getTeamId())
        val jobToUpdate = convertToModel(jobDtoToUpdate)
        try {
            jobRepo.save(jobToUpdate)
        } catch (ex: Exception) {
            val errMsg = "could not update job"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("job")
                .targetId(jobDtoToUpdate.getId())
                .companyId(jobDtoToUpdate.getCompanyId())
                .teamId(jobDtoToUpdate.getTeamId())
                .originalContents(orig.toString())
                .updatedContents(jobDtoToUpdate.toString())
                .build()
        logger.info("updated job", auditLog)
        serviceHelper!!.trackEventAsync("job_updated")
        return jobDtoToUpdate
    }

    fun convertToDto(job: Job?): JobDto? {
        return modelMapper.map(job, JobDto::class.java)
    }

    fun convertToModel(jobDto: JobDto?): Job? {
        return modelMapper.map(jobDto, Job::class.java)
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(JobService::class.java)
    }
}