package xyz.zelda.whoami.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.company.dto.AdminOfList
import xyz.zelda.company.dto.GetAdminOfResponse
import xyz.zelda.company.dto.GetWorkerOfResponse
import xyz.zelda.company.dto.WorkerOfList
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.crypto.Hash
import xyz.zelda.infra.error.ServiceException
import xyz.zelda.whoami.dto.IAmDto
import xyz.zelda.whoami.dto.IntercomSettingsDto
import xyz.zelda.whoami.props.AppProps

@Service
class WhoAmIService {
    @Autowired
    var companyClient: CompanyClient? = null

    @Autowired
    var accountClient: AccountClient? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Autowired
    var appProps: AppProps? = null
    fun findWhoIAm(userId: String?): IAmDto {
        val iAmDto: IAmDto = IAmDto.builder()
                .userId(userId)
                .build()
        var workerOfResponse: GetWorkerOfResponse? = null
        try {
            workerOfResponse = companyClient.getWorkerOf(AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "unable to get worker of list"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!workerOfResponse.isSuccess()) {
            handleErrorAndThrowException(workerOfResponse.getMessage())
        }
        val workerOfList: WorkerOfList = workerOfResponse.getWorkerOfList()
        iAmDto.setWorkerOfList(workerOfList)
        var getAdminOfResponse: GetAdminOfResponse? = null
        try {
            getAdminOfResponse = companyClient.getAdminOf(AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "unable to get admin of list"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!getAdminOfResponse.isSuccess()) {
            handleErrorAndThrowException(getAdminOfResponse.getMessage())
        }
        val adminOfList: AdminOfList = getAdminOfResponse.getAdminOfList()
        iAmDto.setAdminOfList(adminOfList)
        return iAmDto
    }

    fun findIntercomSettings(userId: String?): IntercomSettingsDto {
        val intercomSettingsDto: IntercomSettingsDto = IntercomSettingsDto.builder()
                .appId(appProps.getIntercomAppId())
                .userId(userId)
                .build()
        var genericAccountResponse: GenericAccountResponse? = null
        try {
            genericAccountResponse = accountClient.getAccount(AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "unable to get account"
            handleErrorAndThrowException(ex, errMsg)
        }
        if (!genericAccountResponse.isSuccess()) {
            handleErrorAndThrowException(genericAccountResponse.getMessage())
        }
        val account: AccountDto = genericAccountResponse.getAccount()
        intercomSettingsDto.setName(account.getName())
        intercomSettingsDto.setEmail(account.getEmail())
        intercomSettingsDto.setCreatedAt(account.getMemberSince())
        try {
            val userHash: String = Hash.encode(appProps.getIntercomSigningSecret(), userId)
            intercomSettingsDto.setUserHash(userHash)
        } catch (ex: Exception) {
            val errMsg = "fail to compute user hash"
            handleErrorAndThrowException(ex, errMsg)
        }
        return intercomSettingsDto
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
        val logger: ILogger = SLoggerFactory.getLogger(WhoAmIService::class.java)
    }
}