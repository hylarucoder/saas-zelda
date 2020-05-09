package xyz.zelda.web.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.SyncUserRequest
import xyz.zelda.account.dto.TrackEventRequest
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.exception.ServiceException
import xyz.zelda.company.dto.CompanyDto
import xyz.zelda.mail.client.MailClient
import xyz.zelda.mail.dto.EmailRequest
import xyz.zelda.web.config.AppConfig
import java.net.URI
import java.net.URISyntaxException
import javax.servlet.http.HttpServletRequest

@Service
class HelperService {
    @Autowired
    var accountClient: AccountClient? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Autowired
    var mailClient: MailClient? = null
    fun logError(log: ILogger, errMsg: String?) {
        log.error(errMsg)
        sentryClient.sendMessage(errMsg)
    }

    fun logException(log: ILogger, ex: Exception?, errMsg: String?) {
        log.error(errMsg, ex)
        sentryClient.sendException(ex)
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun trackEventAsync(userId: String?, event: String?) {
        val trackEventRequest: TrackEventRequest = TrackEventRequest.builder()
                .userId(userId).event(event).build()
        var baseResponse: BaseResponse? = null
        try {
            baseResponse = accountClient.trackEvent(trackEventRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to trackEvent through accountClient"
            logException(logger, ex, errMsg)
        }
        if (!baseResponse.isSuccess()) {
            logError(logger, baseResponse.getMessage())
        }
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun syncUserAsync(userId: String?) {
        val request: SyncUserRequest = SyncUserRequest.builder().userId(userId).build()
        accountClient.syncUser(request)
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun sendEmailAsync(a: AccountDto, c: CompanyDto) {
        val emailRequest: EmailRequest = EmailRequest.builder()
                .to("sales@staffjoy.xyz")
                .name("")
                .subject(java.lang.String.format("%s from %s just joined Staffjoy", a.getName(), c.getName()))
                .htmlBody(java.lang.String.format("Name: %s<br>Phone: %s<br>Email: %s<br>Company: %s<br>App: https://app.staffjoy.com/#/companies/%s/employees/",
                        a.getName(),
                        a.getPhoneNumber(),
                        a.getEmail(),
                        c.getName(),
                        c.getId()))
                .build()
        var baseResponse: BaseResponse? = null
        try {
            baseResponse = mailClient.send(emailRequest)
        } catch (ex: Exception) {
            val errMsg = "Unable to send email"
            logException(logger, ex, errMsg)
        }
        if (!baseResponse.isSuccess()) {
            logError(logger, baseResponse.getMessage())
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(HelperService::class.java)
        const val METHOD_POST = "POST"
        fun isPost(request: HttpServletRequest): Boolean {
            return METHOD_POST == request.getMethod()
        }

        @JvmOverloads
        fun buildUrl(scheme: String?, host: String?, path: String? = null): String {
            return try {
                val uri = URI(scheme, host, path, null)
                uri.toString()
            } catch (ex: URISyntaxException) {
                val errMsg = "Internal uri parsing exception."
                logger.error(errMsg)
                throw ServiceException(errMsg, ex)
            }
        }
    }
}