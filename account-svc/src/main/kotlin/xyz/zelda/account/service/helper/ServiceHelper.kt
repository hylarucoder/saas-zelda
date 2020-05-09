package xyz.zelda.account.service.helper

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import com.google.common.collect.Maps
import io.intercom.api.Avatar
import io.intercom.api.CustomAttribute
import io.intercom.api.Event
import io.intercom.api.User
import io.sentry.SentryClient
import lombok.RequiredArgsConstructor
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import xyz.zelda.account.config.AppConfig
import xyz.zelda.account.repo.AccountRepo
import xyz.zelda.bot.client.BotClient
import xyz.zelda.bot.dto.GreetingRequest
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.error.ServiceException
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@RequiredArgsConstructor
@Component
class ServiceHelper {
    private val companyClient: CompanyClient? = null
    private val accountRepo: AccountRepo? = null
    private val sentryClient: SentryClient? = null
    private val botClient: BotClient? = null
    private val envConfig: EnvConfig? = null

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun syncUserAsync(userId: String?) {
        if (envConfig.isDebug()) {
            logger.debug("intercom disabled in dev & test environment")
            return
        }
        val account = accountRepo!!.findAccountById(userId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, String.format("User with id %s not found", userId))
        if (StringUtils.isEmpty(account.getPhoneNumber()) && StringUtils.isEmpty(account.getEmail())) {
            logger.info(java.lang.String.format("skipping sync for user %s because no email or phonenumber", account.getId()))
            return
        }

        // use a map to de-dupe
        val memberships: MutableMap<String, CompanyDto> = HashMap<String, CompanyDto>()
        var workerOfResponse: GetWorkerOfResponse? = null
        workerOfResponse = try {
            companyClient.getWorkerOf(AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "could not fetch workOfList"
            handleException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!workerOfResponse.isSuccess()) {
            handleError(logger, workerOfResponse.getMessage())
            throw ServiceException(workerOfResponse.getMessage())
        }
        val workerOfList: WorkerOfList = workerOfResponse.getWorkerOfList()
        val isWorker: Boolean = workerOfList.getTeams().size() > 0
        for (teamDto in workerOfList.getTeams()) {
            var genericCompanyResponse: GenericCompanyResponse? = null
            genericCompanyResponse = try {
                companyClient.getCompany(AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, teamDto.getCompanyId())
            } catch (ex: Exception) {
                val errMsg = "could not fetch companyDto from teamDto"
                handleException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!genericCompanyResponse.isSuccess()) {
                handleError(logger, genericCompanyResponse.getMessage())
                throw ServiceException(genericCompanyResponse.getMessage())
            }
            val companyDto: CompanyDto = genericCompanyResponse.getCompany()
            memberships[companyDto.getId()] = companyDto
        }
        var getAdminOfResponse: GetAdminOfResponse? = null
        getAdminOfResponse = try {
            companyClient.getAdminOf(AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "could not fetch adminOfList"
            handleException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!getAdminOfResponse.isSuccess()) {
            handleError(logger, getAdminOfResponse.getMessage())
            throw ServiceException(getAdminOfResponse.getMessage())
        }
        val adminOfList: AdminOfList = getAdminOfResponse.getAdminOfList()
        val isAdmin: Boolean = adminOfList.getCompanies().size() > 0
        for (companyDto in adminOfList.getCompanies()) {
            memberships[companyDto.getId()] = companyDto
        }
        val user = User()
        user.setUserId(account.getId())
        user.setEmail(account.getEmail())
        user.setName(account.getName())
        user.setSignedUpAt(account.getMemberSince().toEpochMilli())
        user.setAvatar(Avatar().setImageURL(account.getPhotoUrl()))
        user.setLastRequestAt(Instant.now().toEpochMilli())
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("v2", true))
        user.addCustomAttribute(CustomAttribute.newStringAttribute("phonenumber", account.getPhoneNumber()))
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("confirmed_and_active", account.isConfirmedAndActive()))
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_worker", isWorker))
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_admin", isAdmin))
        user.addCustomAttribute(CustomAttribute.newBooleanAttribute("is_staffjoy_support", account.isSupport()))
        for (companyDto in memberships.values) {
            user.addCompany(Company().setCompanyID(companyDto.getId()).setName(companyDto.getName()))
        }
        syncUserWithIntercom(user, account.getId())
    }

    fun syncUserWithIntercom(user: User?, userId: String) {
        try {
            val params: MutableMap<String, String> = Maps.newHashMap()
            params["user_id"] = userId
            val existing: User = User.find(params)
            if (existing != null) {
                User.update(user)
            } else {
                User.create(user)
            }
            logger.debug("updated intercom")
        } catch (ex: Exception) {
            val errMsg = "fail to create/update user on Intercom"
            handleException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun trackEventAsync(userId: String?, eventName: String) {
        if (envConfig.isDebug()) {
            logger.debug("intercom disabled in dev & test environment")
            return
        }
        val event: Event = Event()
                .setUserID(userId)
                .setEventName("v2_$eventName")
                .setCreatedAt(Instant.now().toEpochMilli())
        try {
            Event.create(event)
        } catch (ex: Exception) {
            val errMsg = "fail to create event on Intercom"
            handleException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        logger.debug("updated intercom")
    }

    fun sendSmsGreeting(userId: String?) {
        var baseResponse: BaseResponse? = null
        baseResponse = try {
            val greetingRequest: GreetingRequest = GreetingRequest.builder().userId(userId).build()
            botClient.sendSmsGreeting(greetingRequest)
        } catch (ex: Exception) {
            val errMsg = "could not send welcome sms"
            handleException(logger, ex, errMsg)
            throw ServiceException(errMsg, ex)
        }
        if (!baseResponse.isSuccess()) {
            handleError(logger, baseResponse.getMessage())
            throw ServiceException(baseResponse.getMessage())
        }
    }

    // for time diff < 2s, treat them as almost same
    fun isAlmostSameInstant(dt1: Instant, dt2: Instant): Boolean {
        var diff = dt1.toEpochMilli() - dt2.toEpochMilli()
        diff = Math.abs(diff)
        return if (diff < TimeUnit.SECONDS.toMillis(1)) {
            true
        } else false
    }

    fun handleError(log: ILogger, errMsg: String?) {
        log.error(errMsg)
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg)
        }
    }

    fun handleException(log: ILogger, ex: Exception?, errMsg: String?) {
        log.error(errMsg, ex)
        if (!envConfig.isDebug()) {
            sentryClient.sendException(ex)
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(ServiceHelper::class.java)
    }
}