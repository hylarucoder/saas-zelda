package xyz.zelda.bot.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.bot.BotConstant
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.error.ServiceException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.json.Json

@Service
class AlertService {
    @Autowired
    private val companyClient: CompanyClient? = null

    @Autowired
    private val helperService: HelperService? = null

    @Autowired
    private val sentryClient: SentryClient? = null
    fun alertNewShift(req: AlertNewShiftRequest) {
        val companyId: String = req.getNewShift().getCompanyId()
        val teamId: String = req.getNewShift().getTeamId()
        val account: AccountDto = helperService!!.getAccountById(req.getUserId())
        val dispatchPreference = helperService.getPreferredDispatch(account)
        if (dispatchPreference == DispatchPreference.DISPATCH_UNAVAILABLE) {
            return
        }
        val companyDto: CompanyDto = helperService.getCompanyById(companyId)
        val teamDto: TeamDto = getTeamByCompanyIdAndTeamId(companyId, teamId)
        val newShiftMsg = printShiftSmsMsg(req.getNewShift(), teamDto.getTimezone())
        var jobName = getJobName(companyId, teamId, req.getNewShift().getJobId())

        // Format name with leading space
        if (!StringUtils.isEmpty(jobName)) {
            jobName = " $jobName"
        }
        val greet = HelperService.getGreet(account.getName())
        val companyName: String = companyDto.getName()
        if (dispatchPreference == DispatchPreference.DISPATCH_EMAIL) {
            val htmlBody: String = java.lang.String.format(BotConstant.ALERT_NEW_SHIFT_EMAIL_TEMPLATE,
                    greet, companyName, jobName, newShiftMsg)
            val subject = "New Shift Alert"
            val email: String = account.getEmail()
            val name: String = account.getName()
            helperService.sendMail(email, name, subject, htmlBody)
        } else { // sms
            val templateParam: String = Json.createObjectBuilder()
                    .add("greet", greet)
                    .add("company_name", companyName)
                    .add("job_name", jobName)
                    .add("shift_msg", newShiftMsg)
                    .build()
                    .toString()
            val phoneNumber: String = account.getPhoneNumber()

            // TODO crate sms template on aliyun then update constant
//        String msg = String.format("%s Your %s manager just published a new%s shift for you: \n%s",
////                greet, company.getName(), jobName, newShiftMsg);
            helperService.sendSms(phoneNumber, BotConstant.ALERT_NEW_SHIFT_SMS_TEMPLATE_CODE, templateParam)
        }
    }

    fun alertNewShifts(req: AlertNewShiftsRequest) {
        val shiftDtos: List<ShiftDto> = req.getNewShifts()
        if (shiftDtos.size == 0) {
            throw ServiceException(ResultCode.PARAM_MISS, "empty shifts list in request")
        }
        val companyId: String = shiftDtos[0].getCompanyId()
        val teamId: String = shiftDtos[0].getTeamId()
        val account: AccountDto = helperService!!.getAccountById(req.getUserId())
        val dispatchPreference = helperService.getPreferredDispatch(account)
        if (dispatchPreference == DispatchPreference.DISPATCH_UNAVAILABLE) {
            return
        }
        val companyDto: CompanyDto = helperService.getCompanyById(companyId)
        val teamDto: TeamDto = getTeamByCompanyIdAndTeamId(companyId, teamId)
        var newShiftsMsg = ""
        val separator = if (dispatchPreference == DispatchPreference.DISPATCH_SMS) "\n" else "<br/><br/>"
        for (shiftDto in shiftDtos) {
            val newShiftMsg = printShiftSmsMsg(shiftDto, teamDto.getTimezone())
            var jobName = getJobName(companyId, teamId, shiftDto.getJobId())

            // Format name with leading space
            if (!StringUtils.isEmpty(jobName)) {
                jobName = " $jobName"
            }
            newShiftsMsg += String.format("%s%s%s", newShiftMsg, jobName, separator)
        }
        val greet = HelperService.getGreet(account.getName())
        val companyName: String = companyDto.getName()
        val numberOfShifts = shiftDtos.size
        if (dispatchPreference == DispatchPreference.DISPATCH_EMAIL) {
            val htmlBody: String = java.lang.String.format(BotConstant.ALERT_NEW_SHIFTS_EMAIL_TEMPLATE,
                    greet, companyName, numberOfShifts, newShiftsMsg)
            val subject = "New Shifts Alert"
            val email: String = account.getEmail()
            val name: String = account.getName()
            helperService.sendMail(email, name, subject, htmlBody)
        } else { // sms
            val templateParam: String = Json.createObjectBuilder()
                    .add("greet", greet)
                    .add("company_name", companyName)
                    .add("shifts_size", numberOfShifts)
                    .add("shifts_msg", newShiftsMsg)
                    .build()
                    .toString()
            val phoneNumber: String = account.getPhoneNumber()

            // TODO crate sms template on aliyun then update constant
//        String msg = String.format("%s Your %s manager just published %d new shifts that you are working: \n%s",
//                greet, companyDto.getName(), shifts.size(), newShiftsMsg);
            helperService.sendSms(phoneNumber, BotConstant.ALERT_NEW_SHIFTS_SMS_TEMPLATE_CODE, templateParam)
        }
    }

    fun alertRemovedShift(req: AlertRemovedShiftRequest) {
        val companyId: String = req.getOldShift().getCompanyId()
        val teamId: String = req.getOldShift().getTeamId()
        val account: AccountDto = helperService!!.getAccountById(req.getUserId())
        val dispatchPreference = helperService.getPreferredDispatch(account)
        if (dispatchPreference == DispatchPreference.DISPATCH_UNAVAILABLE) {
            return
        }
        val companyDto: CompanyDto = helperService.getCompanyById(companyId)
        val teamDto: TeamDto = getTeamByCompanyIdAndTeamId(companyId, teamId)
        val workerShiftListRequest: WorkerShiftListRequest = WorkerShiftListRequest.builder()
                .companyId(companyId)
                .teamId(teamId)
                .workerId(req.getUserId())
                .shiftStartAfter(Instant.now())
                .shiftStartBefore(Instant.now().plus(BotConstant.SHIFT_WINDOW, ChronoUnit.DAYS))
                .build()
        val shiftList: ShiftList = listWorkerShifts(workerShiftListRequest)
        var newShiftsMsg = ""
        val separator = if (dispatchPreference == DispatchPreference.DISPATCH_SMS) "\n" else "<br/><br/>"
        for (shiftDto in shiftList.getShifts()) {
            val newShiftMsg = printShiftSmsMsg(shiftDto, teamDto.getTimezone())
            newShiftsMsg += String.format("%s%s", newShiftMsg, separator)
        }
        val greet = HelperService.getGreet(account.getName())
        val companyName: String = companyDto.getName()
        if (dispatchPreference == DispatchPreference.DISPATCH_EMAIL) {
            val htmlBody: String = java.lang.String.format(BotConstant.ALERT_REMOVED_SHIFT_EMAIL_TEMPLATE,
                    greet, companyName, newShiftsMsg)
            val subject = "Removed Shift Alert"
            val email: String = account.getEmail()
            val name: String = account.getName()
            helperService.sendMail(email, name, subject, htmlBody)
        } else {
            // TODO crate sms template on aliyun then update constant
//        String msg = String.format("%s Your %s manager just removed you from a shift, so you are no longer working on it. Here is your new schedule: \n%s",
//                greet, company.getName(), newShiftsMsg);
            val templateParam: String = Json.createObjectBuilder()
                    .add("greet", greet)
                    .add("company_name", companyDto.getName())
                    .add("shifts_msg", newShiftsMsg)
                    .build()
                    .toString()
            val phoneNumber: String = account.getPhoneNumber()
            helperService.sendSms(phoneNumber, BotConstant.ALERT_REMOVED_SHIFT_SMS_TEMPLATE_CODE, templateParam)
        }
    }

    fun alertRemovedShifts(req: AlertRemovedShiftsRequest) {
        val shiftDtos: List<ShiftDto> = req.getOldShifts()
        if (shiftDtos.size == 0) {
            throw ServiceException(ResultCode.PARAM_MISS, "empty shifts list in request")
        }
        val companyId: String = shiftDtos[0].getCompanyId()
        val teamId: String = shiftDtos[0].getTeamId()
        val account: AccountDto = helperService!!.getAccountById(req.getUserId())
        val dispatchPreference = helperService.getPreferredDispatch(account)
        if (dispatchPreference == DispatchPreference.DISPATCH_UNAVAILABLE) {
            return
        }
        val companyDto: CompanyDto = helperService.getCompanyById(companyId)
        val teamDto: TeamDto = getTeamByCompanyIdAndTeamId(companyId, teamId)
        val workerShiftListRequest: WorkerShiftListRequest = WorkerShiftListRequest.builder()
                .companyId(companyId)
                .teamId(teamId)
                .workerId(req.getUserId())
                .shiftStartAfter(Instant.now())
                .shiftStartBefore(Instant.now().plus(BotConstant.SHIFT_WINDOW, ChronoUnit.DAYS))
                .build()
        val shiftList: ShiftList = listWorkerShifts(workerShiftListRequest)
        var newShiftsMsg = ""
        val separator = if (dispatchPreference == DispatchPreference.DISPATCH_SMS) "\n" else "<br/><br/>"
        for (shiftDto in shiftList.getShifts()) {
            val newShiftMsg = printShiftSmsMsg(shiftDto, teamDto.getTimezone())
            newShiftsMsg += String.format("%s%s", newShiftMsg, separator)
        }
        val greet = HelperService.getGreet(account.getName())
        val companyName: String = companyDto.getName()
        val numberOfShifts = shiftDtos.size
        if (dispatchPreference == DispatchPreference.DISPATCH_EMAIL) {
            val htmlBody: String = java.lang.String.format(BotConstant.ALERT_REMOVED_SHIFTS_EMAIL_TEMPLATE,
                    greet, companyName, numberOfShifts, newShiftsMsg)
            val subject = "Removed Shifts Alert"
            val email: String = account.getEmail()
            val name: String = account.getName()
            helperService.sendMail(email, name, subject, htmlBody)
        } else { // sms

            // TODO create sms template then update code
//        String msg = String.format("%s Your %s manager just removed %d of your shifts so you are no longer working on it. \n Your new shifts are: \n%s",
//                greet, companyDto.getName(), shifts.size() newShiftsMsg);
            val templateParam: String = Json.createObjectBuilder()
                    .add("greet", greet)
                    .add("company_name", companyName)
                    .add("shifts_size", numberOfShifts)
                    .add("shifts_msg", newShiftsMsg)
                    .build()
                    .toString()
            val phoneNumber: String = account.getPhoneNumber()
            helperService.sendSms(phoneNumber, BotConstant.ALERT_REMOVED_SHIFTS_SMS_TEMPLATE_CODE, templateParam)
        }
    }

    fun alertChangedShift(req: AlertChangedShiftRequest) {
        val companyId: String = req.getOldShift().getCompanyId()
        val teamId: String = req.getOldShift().getTeamId()
        val account: AccountDto = helperService!!.getAccountById(req.getUserId())
        val dispatchPreference = helperService.getPreferredDispatch(account)
        if (dispatchPreference == DispatchPreference.DISPATCH_UNAVAILABLE) {
            return
        }
        val companyDto: CompanyDto = helperService.getCompanyById(companyId)
        val teamDto: TeamDto = getTeamByCompanyIdAndTeamId(companyId, teamId)
        var oldShiftMsg = printShiftSmsMsg(req.getOldShift(), teamDto.getTimezone())
        val oldJobName = getJobName(companyId, teamId, req.getNewShift().getJobId())

        // Format name with leading space.
        if (!StringUtils.isEmpty(oldJobName)) {
            oldShiftMsg += String.format(" (%s)", oldJobName)
        }
        var newShiftMsg = printShiftSmsMsg(req.getNewShift(), teamDto.getTimezone())
        val newJobName = getJobName(companyId, teamId, req.getOldShift().getJobId())

        // Format name with leading space.
        if (!StringUtils.isEmpty(newJobName)) {
            newShiftMsg += String.format(" (%s)", newJobName)
        }
        val greet = HelperService.getGreet(account.getName())
        val companyName: String = companyDto.getName()
        if (dispatchPreference == DispatchPreference.DISPATCH_EMAIL) {
            val htmlBody: String = java.lang.String.format(BotConstant.ALERT_CHANGED_SHIFT_EMAIL_TEMPLATE,
                    greet, companyName, oldShiftMsg, newShiftMsg)
            val subject = "Changed Shift Alert"
            val email: String = account.getEmail()
            val name: String = account.getName()
            helperService.sendMail(email, name, subject, htmlBody)
        } else { // sms

            // TODO create sms template on aliyun then update constant
//        String msg = String.format("%s Your %s manager just changed your shift: \nOld: %s\nNew:%s",
//                greet, companyDto.getName(), oldShiftMsg, newShiftMsg);
            val templateParam: String = Json.createObjectBuilder()
                    .add("greet", greet)
                    .add("company_name", companyName)
                    .add("old_shift_msg", oldShiftMsg)
                    .add("new_shift_msg", newShiftMsg)
                    .build()
                    .toString()
            val phoneNumber: String = account.getPhoneNumber()
            helperService.sendSms(phoneNumber, BotConstant.ALERT_CHANGED_SHIFT_SMS_TEMPLATE_CODE, templateParam)
        }
    }

    private fun listWorkerShifts(workerShiftListRequest: WorkerShiftListRequest): ShiftList {
        var shiftListResponse: GenericShiftListResponse? = null
        shiftListResponse = try {
            companyClient.listWorkerShifts(AuthConstant.AUTHORIZATION_BOT_SERVICE, workerShiftListRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to list worker shifts"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!shiftListResponse.isSuccess()) {
            logger.error(shiftListResponse.getMessage())
            sentryClient.sendMessage(shiftListResponse.getMessage())
            throw ServiceException(shiftListResponse.getMessage())
        }
        return shiftListResponse.getShiftList()
    }

    private fun getTeamByCompanyIdAndTeamId(companyId: String, teamId: String): TeamDto {
        var teamResponse: GenericTeamResponse? = null
        teamResponse = try {
            companyClient.getTeam(AuthConstant.AUTHORIZATION_BOT_SERVICE, companyId, teamId)
        } catch (ex: Exception) {
            val errMsg = "fail to get team"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!teamResponse.isSuccess()) {
            logger.error(teamResponse.getMessage())
            sentryClient.sendMessage(teamResponse.getMessage())
            throw ServiceException(teamResponse.getMessage())
        }
        return teamResponse.getTeam()
    }

    private fun printShiftSmsMsg(shiftDto: ShiftDto, tz: String): String {
        val startTimeFormatter = DateTimeFormatter.ofPattern(BotConstant.SMS_START_TIME_FORMAT)
                .withZone(ZoneId.of(tz))
        val endTimeFormatter = DateTimeFormatter.ofPattern(BotConstant.SMS_STOP_TIME_FORMAT)
                .withZone(ZoneId.of(tz))
        val startTime = startTimeFormatter.format(shiftDto.getStart())
        val endTime = endTimeFormatter.format(shiftDto.getStop())
        return java.lang.String.format(BotConstant.SMS_SHIFT_FORMAT, startTime, endTime)
    }

    // JobName returns the name of a job, given its id
    private fun getJobName(companyId: String, teamId: String, jobId: String): String {
        if (StringUtils.isEmpty(jobId)) {
            return ""
        }
        var jobResponse: GenericJobResponse? = null
        jobResponse = try {
            companyClient.getJob(AuthConstant.AUTHORIZATION_BOT_SERVICE, jobId, companyId, teamId)
        } catch (ex: Exception) {
            val errMsg = "fail to get job"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!jobResponse.isSuccess()) {
            logger.error(jobResponse.getMessage())
            sentryClient.sendMessage(jobResponse.getMessage())
            throw ServiceException(jobResponse.getMessage())
        }
        val jobDto: JobDto = jobResponse.getJob()
        return jobDto.getName()
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(AlertService::class.java)
    }
}