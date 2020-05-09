package xyz.zelda.company.service.helper

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import xyz.zelda.company.dto.ShiftDto
import xyz.zelda.company.model.Shift
import xyz.zelda.company.repo.ShiftRepo
import xyz.zelda.company.service.DirectoryService
import xyz.zelda.company.service.JobService
import xyz.zelda.company.service.TeamService
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.exception.ServiceException

@Component
class ShiftHelper {
    @Autowired
    var teamService: TeamService? = null

    @Autowired
    var directoryService: DirectoryService? = null

    @Autowired
    var jobService: JobService? = null

    @Autowired
    var shiftRepo: ShiftRepo? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null

    @Autowired
    var modelMapper: ModelMapper? = null

    //    @Async("asyncExecutor")
    //    public void updateShiftAsync(ShiftDto shiftDto) {
    //        updateShift(shiftDto, true);
    //    }
    fun updateShift(shiftDtoToUpdate: ShiftDto?, suppressNotification: Boolean): ShiftDto? {
        // validate and will throw exception if not exist
        val orig: ShiftDto? = getShift(shiftDtoToUpdate.getId(), shiftDtoToUpdate.getTeamId(), shiftDtoToUpdate.getCompanyId())
        if (orig.equals(shiftDtoToUpdate)) { // no change
            return shiftDtoToUpdate
        }
        if (!StringUtils.isEmpty(shiftDtoToUpdate.getUserId())) {
            // validate and will throw exception if not exist
            directoryService!!.getDirectoryEntry(shiftDtoToUpdate.getCompanyId(), shiftDtoToUpdate.getUserId())
        }
        if (!StringUtils.isEmpty(shiftDtoToUpdate.getJobId())) {
            // validate and will throw exception if not exist
            jobService!!.getJob(shiftDtoToUpdate.getJobId(), shiftDtoToUpdate.getCompanyId(), shiftDtoToUpdate.getTeamId())
        }
        val shiftToUpdate = convertToModel(shiftDtoToUpdate)
        try {
            shiftRepo.save(shiftToUpdate)
        } catch (ex: Exception) {
            val errMsg = "could not update the shift"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("shift")
                .targetId(shiftDtoToUpdate.getId())
                .companyId(shiftDtoToUpdate.getCompanyId())
                .teamId(shiftDtoToUpdate.getTeamId())
                .originalContents(orig.toString())
                .updatedContents(shiftDtoToUpdate.toString())
                .build()
        logger.info("updated shift", auditLog)
        serviceHelper!!.trackEventAsync("shift_updated")
        if (!orig.isPublished() && shiftDtoToUpdate.isPublished()) {
            serviceHelper!!.trackEventAsync("shift_published")
        }
        if (!suppressNotification) {
            serviceHelper!!.updateShiftNotificationAsync(orig, shiftDtoToUpdate)
        }
        return shiftDtoToUpdate
    }

    fun getShift(shiftId: String?, teamId: String?, companyId: String?): ShiftDto? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(companyId, teamId)
        val shift = shiftRepo!!.findShiftById(shiftId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "shift with specified id not found")
        val shiftDto: ShiftDto? = convertToDto(shift)
        shiftDto.setCompanyId(companyId)
        return shiftDto
    }

    fun convertToDto(shift: Shift?): ShiftDto? {
        return modelMapper.map(shift, ShiftDto::class.java)
    }

    fun convertToModel(shiftDto: ShiftDto?): Shift? {
        return modelMapper.map(shiftDto, Shift::class.java)
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(ShiftHelper::class.java)
    }
}