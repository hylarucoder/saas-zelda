package xyz.zelda.company.service.helper

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.TrackEventRequest
import xyz.zelda.bot.client.BotClient
import xyz.zelda.company.config.AppConfig
import xyz.zelda.company.dto.ShiftDto
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.error.ServiceException
import java.time.Instant

@Component
class ServiceHelper {
    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val botClient: BotClient? = null

    @Autowired
    private val sentryClient: SentryClient? = null

    @Autowired
    private val envConfig: EnvConfig? = null

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun trackEventAsync(event: String?) {
        val userId: String = AuthContext.getUserId()
        if (StringUtils.isEmpty(userId)) {
            // Not an action performed by a normal user
            // (noop - not an view)
            return
        }
        val trackEventRequest: TrackEventRequest = TrackEventRequest.builder()
                .userId(userId).event(event).build()
        var resp: BaseResponse? = null
        try {
            resp = accountClient.trackEvent(trackEventRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to trackEvent through accountClient"
            handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!resp.isSuccess()) {
            handleErrorAndThrowException(logger, resp.getMessage())
        }
    }

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun onboardWorkerAsync(onboardWorkerRequest: OnboardWorkerRequest?) {
        var baseResponse: BaseResponse? = null
        try {
            baseResponse = botClient.onboardWorker(onboardWorkerRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to call onboardWorker through botClient"
            handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!baseResponse.isSuccess()) {
            handleErrorAndThrowException(logger, baseResponse.getMessage())
        }
    }

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun alertNewShiftAsync(alertNewShiftRequest: AlertNewShiftRequest?) {
        var baseResponse: BaseResponse? = null
        try {
            baseResponse = botClient.alertNewShift(alertNewShiftRequest)
        } catch (ex: Exception) {
            val errMsg = "failed to alert worker about new shift"
            handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!baseResponse.isSuccess()) {
            handleErrorAndThrowException(logger, baseResponse.getMessage())
        }
    }

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun alertRemovedShiftAsync(alertRemovedShiftRequest: AlertRemovedShiftRequest?) {
        var baseResponse: BaseResponse? = null
        try {
            baseResponse = botClient.alertRemovedShift(alertRemovedShiftRequest)
        } catch (ex: Exception) {
            val errMsg = "failed to alert worker about removed shift"
            handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!baseResponse.isSuccess()) {
            handleErrorAndThrowException(logger, baseResponse.getMessage())
        }
    }

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun buildShiftNotificationAsync(notifs: MutableMap<String?, MutableList<ShiftDto?>?>?, published: Boolean) {
        for (entry in notifs!!.entries) {
            val userId = entry.key
            val shiftDtos: MutableList<ShiftDto?>? = entry.value
            if (published) {
                // alert published
                val alertNewShiftsRequest: AlertNewShiftsRequest = AlertNewShiftsRequest.builder()
                        .userId(userId)
                        .newShifts(shiftDtos)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertNewShifts(alertNewShiftsRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about new shifts"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            } else {
                // alert removed
                val alertRemovedShiftsRequest: AlertRemovedShiftsRequest = AlertRemovedShiftsRequest.builder()
                        .userId(userId)
                        .oldShifts(shiftDtos)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertRemovedShifts(alertRemovedShiftsRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about removed shifts"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
        }
    }

    // send bot notifications
    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun updateShiftNotificationAsync(origShiftDto: ShiftDto?, shiftDtoToUpdte: ShiftDto?) {
        if (!origShiftDto.isPublished() && shiftDtoToUpdte.isPublished()) {
            if (shiftDtoToUpdte.getStart().isAfter(Instant.now()) &&
                    !StringUtils.isEmpty(shiftDtoToUpdte.getUserId())) {
                // looks like a new shift
                val alertNewShiftRequest: AlertNewShiftRequest = AlertNewShiftRequest.builder()
                        .userId(shiftDtoToUpdte.getUserId())
                        .newShift(shiftDtoToUpdte)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertNewShift(alertNewShiftRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about new shift"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
            return
        }
        if (origShiftDto.isPublished() && !shiftDtoToUpdte.isPublished()) {
            if (shiftDtoToUpdte.getStart().isAfter(Instant.now()) &&
                    !StringUtils.isEmpty(origShiftDto.getUserId())) {
                // removed a shift
                val alertRemovedShiftRequest: AlertRemovedShiftRequest = AlertRemovedShiftRequest.builder()
                        .userId(origShiftDto.getUserId())
                        .oldShift(origShiftDto)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertRemovedShift(alertRemovedShiftRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about removed shift"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
            return
        }
        if (!origShiftDto.isPublished() && !shiftDtoToUpdte.isPublished()) {
            // NOOP - basically return
            return
        }
        if (!StringUtils.isEmpty(origShiftDto.getUserId()) && origShiftDto.getUserId().equals(shiftDtoToUpdte.getUserId())) {
            if (shiftDtoToUpdte.getStart().isAfter(Instant.now())) {
                val alertChangedShiftRequest: AlertChangedShiftRequest = AlertChangedShiftRequest.builder()
                        .userId(origShiftDto.getUserId())
                        .oldShift(origShiftDto)
                        .newShift(shiftDtoToUpdte)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertChangedShift(alertChangedShiftRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about changed shift"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
            return
        }
        if (!origShiftDto.getUserId().equals(shiftDtoToUpdte.getUserId())) {
            if (!StringUtils.isEmpty(origShiftDto.getUserId()) && origShiftDto.getStart().isAfter(Instant.now())) {
                val alertRemovedShiftRequest: AlertRemovedShiftRequest = AlertRemovedShiftRequest.builder()
                        .userId(origShiftDto.getUserId())
                        .oldShift(origShiftDto)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertRemovedShift(alertRemovedShiftRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about removed shift"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
            if (!StringUtils.isEmpty(shiftDtoToUpdte.getUserId()) && shiftDtoToUpdte.getStart().isAfter(Instant.now())) {
                val alertNewShiftRequest: AlertNewShiftRequest = AlertNewShiftRequest.builder()
                        .userId(shiftDtoToUpdte.getUserId())
                        .newShift(shiftDtoToUpdte)
                        .build()
                var baseResponse: BaseResponse? = null
                try {
                    baseResponse = botClient.alertNewShift(alertNewShiftRequest)
                } catch (ex: Exception) {
                    val errMsg = "failed to alert worker about new shift"
                    handleErrorAndThrowException(logger, ex, errMsg)
                }
                if (!baseResponse.isSuccess()) {
                    handleErrorAndThrowException(logger, baseResponse.getMessage())
                }
            }
            return
        }
        logger.error(java.lang.String.format("unable to determine updated shift messaging - orig %s new %s", origShiftDto, shiftDtoToUpdte))
    }

    fun handleErrorAndThrowException(log: ILogger?, errMsg: String?) {
        log.error(errMsg)
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg)
        }
        throw ServiceException(errMsg)
    }

    fun handleErrorAndThrowException(log: ILogger?, ex: Exception?, errMsg: String?) {
        log.error(errMsg, ex)
        if (!envConfig.isDebug()) {
            sentryClient.sendException(ex)
        }
        throw ServiceException(errMsg, ex)
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(ServiceHelper::class.java)
    }
}