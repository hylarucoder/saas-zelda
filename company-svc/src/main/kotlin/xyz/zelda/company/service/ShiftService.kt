package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import xyz.zelda.bot.dto.AlertNewShiftRequest
import xyz.zelda.bot.dto.AlertRemovedShiftRequest
import xyz.zelda.company.model.Shift
import xyz.zelda.company.repo.ShiftRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.company.service.helper.ShiftHelper
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import java.time.Instant
import java.util.*

@Service
class ShiftService {
    @Autowired
    var shiftRepo: ShiftRepo? = null

    @Autowired
    var teamService: TeamService? = null

    @Autowired
    var jobService: JobService? = null

    @Autowired
    var directoryService: DirectoryService? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null

    @Autowired
    var shiftHelper: ShiftHelper? = null

    @Autowired
    var modelMapper: ModelMapper? = null
    fun createShift(req: CreateShiftRequest?): ShiftDto? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(req.getCompanyId(), req.getTeamId())
        if (!StringUtils.isEmpty(req.getJobId())) {
            // validate and will throw exception if not exist
            jobService!!.getJob(req.getJobId(), req.getCompanyId(), req.getTeamId())
        }
        if (!StringUtils.isEmpty(req.getUserId())) {
            directoryService!!.getDirectoryEntry(req.getCompanyId(), req.getUserId())
        }
        val shift: Shift = builder()
                .teamId(req.getTeamId())
                .jobId(req.getJobId())
                .start(req.getStart())
                .stop(req.getStop())
                .published(req.isPublished())
                .userId(req.getUserId())
                .build()
        try {
            shiftRepo.save(shift)
        } catch (ex: Exception) {
            val errMsg = "could not create shift"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("shift")
                .targetId(shift.getId())
                .companyId(req.getCompanyId())
                .teamId(req.getTeamId())
                .updatedContents(shift.toString())
                .build()
        logger.info("created shift", auditLog)
        val shiftDto: ShiftDto? = shiftHelper!!.convertToDto(shift)
        shiftDto.setCompanyId(req.getCompanyId())
        if (!StringUtils.isEmpty(shift.getUserId()) && shift.isPublished()) {
            val alertNewShiftRequest: AlertNewShiftRequest = AlertNewShiftRequest.builder()
                    .userId(shiftDto.getUserId())
                    .newShift(shiftDto)
                    .build()
            serviceHelper!!.alertNewShiftAsync(alertNewShiftRequest)
        }
        serviceHelper!!.trackEventAsync("shift_created")
        if (req.isPublished()) {
            serviceHelper!!.trackEventAsync("shift_published")
        }
        return shiftDto
    }

    fun listWorkerShifts(req: WorkerShiftListRequest?): ShiftList? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(req.getCompanyId(), req.getTeamId())
        val shiftList: ShiftList = ShiftList.builder()
                .shiftStartAfter(req.getShiftStartAfter())
                .shiftStartBefore(req.getShiftStartBefore())
                .build()
        val shifts = shiftRepo!!.listWorkerShifts(req.getTeamId(), req.getWorkerId(), req.getShiftStartAfter(), req.getShiftStartBefore())
        return convertToShiftList(shiftList, shifts, req.getCompanyId())
    }

    fun listShifts(req: ShiftListRequest?): ShiftList? {
        // validate and will throw exception if not exist
        teamService!!.getTeamWithCompanyIdValidation(req.getCompanyId(), req.getTeamId())
        val shiftList: ShiftList = ShiftList.builder()
                .shiftStartAfter(req.getShiftStartAfter())
                .shiftStartBefore(req.getShiftStartBefore())
                .build()
        var shifts: MutableList<Shift?>? = null
        if (!StringUtils.isEmpty(req.getUserId()) && StringUtils.isEmpty(req.getJobId())) {
            shifts = shiftRepo!!.listWorkerShifts(req.getTeamId(), req.getUserId(), req.getShiftStartAfter(), req.getShiftStartBefore())
        }
        if (!StringUtils.isEmpty(req.getJobId()) && StringUtils.isEmpty(req.getUserId())) {
            shifts = shiftRepo!!.listShiftByJobId(req.getTeamId(), req.getJobId(), req.getShiftStartAfter(), req.getShiftStartBefore())
        }
        if (!StringUtils.isEmpty(req.getJobId()) && !StringUtils.isEmpty(req.getUserId())) {
            shifts = shiftRepo!!.listShiftByUserIdAndJobId(req.getTeamId(), req.getUserId(), req.getJobId(), req.getShiftStartAfter(), req.getShiftStartBefore())
        }
        if (StringUtils.isEmpty(req.getJobId()) && StringUtils.isEmpty(req.getUserId())) {
            shifts = shiftRepo!!.listShiftByTeamIdOnly(req.getTeamId(), req.getShiftStartAfter(), req.getShiftStartBefore())
        }
        return convertToShiftList(shiftList, shifts, req.getCompanyId())
    }

    private fun convertToShiftList(shiftList: ShiftList?, shifts: MutableList<Shift?>?, companyId: String?): ShiftList? {
        for (shift in shifts!!) {
            val shiftDto: ShiftDto? = shiftHelper!!.convertToDto(shift)
            shiftDto.setCompanyId(companyId)
            shiftList.getShifts().add(shiftDto)
        }
        return shiftList
    }

    private fun quickTime(startTime: Long): Long {
        val endTime = System.currentTimeMillis()
        return (endTime - startTime) / 1000
    }

    fun bulkPublishShifts(req: BulkPublishShiftsRequest?): ShiftList? {
        val startTime = System.currentTimeMillis()
        logger.info(String.format("time so far %d", quickTime(startTime)))
        val shiftListRequest: ShiftListRequest = ShiftListRequest.builder()
                .companyId(req.getCompanyId())
                .teamId(req.getTeamId())
                .userId(req.getUserId())
                .jobId(req.getJobId())
                .shiftStartAfter(req.getShiftStartAfter())
                .shiftStartBefore(req.getShiftStartBefore())
                .build()
        val orig: ShiftList? = listShifts(shiftListRequest)
        val shiftList: ShiftList = ShiftList.builder()
                .shiftStartAfter(req.getShiftStartAfter())
                .shiftStartBefore(req.getShiftStartBefore())
                .build()

        // Keep track of notifications - user to orig shift
        val notifs: MutableMap<String?, MutableList<ShiftDto?>?> = HashMap<String?, MutableList<ShiftDto?>?>()
        logger.info(String.format("before shifts update %d", quickTime(startTime)))
        for (shiftDto in orig.getShifts()) {
            // keep track of what changed for messaging purpose
            if (!StringUtils.isEmpty(shiftDto.getUserId()) && shiftDto.isPublished() !== req.isPublished() &&
                    shiftDto.getStart().isAfter(Instant.now())) {
                var shiftDtos: MutableList<ShiftDto?>? = notifs[shiftDto.getUserId()]
                if (shiftDtos == null) {
                    shiftDtos = ArrayList<ShiftDto?>()
                    notifs[shiftDto.getUserId()] = shiftDtos
                }
                val copy: ShiftDto = shiftDto.toBuilder().build()
                shiftDtos!!.add(copy)
            }
            // do the change
            shiftDto.setPublished(req.isPublished())

            //shiftHelper.updateShiftAsync(shiftDto);
            shiftHelper!!.updateShift(shiftDto, true)
            shiftList.getShifts().add(shiftDto)
        }
        logger.info(String.format("before shifts notifications %d", quickTime(startTime)))
        serviceHelper!!.buildShiftNotificationAsync(notifs, req.isPublished())
        logger.info(String.format("total time %d", quickTime(startTime)))
        return shiftList
    }

    fun getShift(shiftId: String?, teamId: String?, companyId: String?): ShiftDto? {
        return shiftHelper!!.getShift(shiftId, teamId, companyId)
    }

    fun updateShift(shiftDtoToUpdate: ShiftDto?): ShiftDto? {
        return shiftHelper!!.updateShift(shiftDtoToUpdate, false)
    }

    fun deleteShift(shiftId: String?, teamId: String?, companyId: String?) {
        val orig: ShiftDto? = getShift(shiftId, teamId, companyId)
        try {
            shiftRepo!!.deleteShiftById(shiftId)
        } catch (ex: Exception) {
            val errMsg = "failed to delete shift"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("shift")
                .targetId(shiftId)
                .companyId(companyId)
                .teamId(teamId)
                .originalContents(orig.toString())
                .build()
        logger.info("deleted shift", auditLog)
        if (!StringUtils.isEmpty(orig.getUserId()) && orig.isPublished() && orig.getStart().isAfter(Instant.now())) {
            val alertRemovedShiftRequest: AlertRemovedShiftRequest = AlertRemovedShiftRequest.builder()
                    .userId(orig.getUserId())
                    .oldShift(orig)
                    .build()
            serviceHelper!!.alertRemovedShiftAsync(alertRemovedShiftRequest)
        }
        serviceHelper!!.trackEventAsync("shift_deleted")
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(ShiftService::class.java)
    }
}