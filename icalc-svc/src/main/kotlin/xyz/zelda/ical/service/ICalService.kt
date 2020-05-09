package xyz.zelda.ical.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.ical.model.Cal
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.error.ServiceException
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ICalService {
    @Autowired
    private val companyClient: CompanyClient? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Autowired
    var envConfig: EnvConfig? = null
    fun getCalByUserId(userId: String?): Cal {
        var workerResponse: GenericWorkerResponse? = null
        try {
            workerResponse = companyClient.getWorkerTeamInfo(AuthConstant.AUTHORIZATION_ICAL_SERVICE, null, userId)
        } catch (ex: Exception) {
            val errMsg = "unable to get team info"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!workerResponse.isSuccess()) {
            handleErrorAndThrowException(workerResponse.getMessage())
        }
        val workerDto: WorkerDto = workerResponse.getWorker()
        var genericCompanyResponse: GenericCompanyResponse? = null
        try {
            genericCompanyResponse = companyClient.getCompany(AuthConstant.AUTHORIZATION_ICAL_SERVICE, workerDto.getCompanyId())
        } catch (ex: Exception) {
            val errMsg = "unable to get company"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!genericCompanyResponse.isSuccess()) {
            handleErrorAndThrowException(genericCompanyResponse.getMessage())
        }
        val companyDto: CompanyDto = genericCompanyResponse.getCompany()
        val workerShiftListRequest: WorkerShiftListRequest = WorkerShiftListRequest.builder()
                .companyId(workerDto.getCompanyId())
                .teamId(workerDto.getTeamId())
                .workerId(workerDto.getUserId())
                .shiftStartAfter(Instant.now().minus(30, ChronoUnit.DAYS))
                .shiftStartBefore(Instant.now().plus(90, ChronoUnit.DAYS))
                .build()
        var shiftListResponse: GenericShiftListResponse? = null
        try {
            shiftListResponse = companyClient.listWorkerShifts(AuthConstant.AUTHORIZATION_ICAL_SERVICE, workerShiftListRequest)
        } catch (ex: Exception) {
            val errMsg = "unable to get worker shifts"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!shiftListResponse.isSuccess()) {
            handleErrorAndThrowException(shiftListResponse.getMessage())
        }
        val shiftList: ShiftList = shiftListResponse.getShiftList()
        return builder()
                .companyName(companyDto.getName())
                .shiftList(shiftList.getShifts())
                .build()
    }

    fun handleErrorAndThrowException(errMsg: String?) {
        logger.error(errMsg)
        sentryClient.sendMessage(errMsg)
        throw ServiceException(errMsg)
    }

    fun handleErrorAndThrowException(ex: Exception?, errMsg: String?) {
        logger.error(errMsg, ex)
        sentryClient.sendException(ex)
        throw ServiceException(errMsg, ex)
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(ICalService::class.java)
    }
}