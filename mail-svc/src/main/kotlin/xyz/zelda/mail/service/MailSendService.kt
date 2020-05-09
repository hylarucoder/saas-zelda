package xyz.zelda.mail.service

import com.aliyuncs.IAcsClient
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse
import com.aliyuncs.exceptions.ClientException
import com.github.structlog4j.ILogger
import com.github.structlog4j.IToLog
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import io.sentry.context.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.env.EnvConstant
import xyz.zelda.mail.MailConstant
import xyz.zelda.mail.config.AppConfig
import xyz.zelda.mail.dto.EmailRequest
import java.lang.String

@Service
class MailSendService {
    @Autowired
    var envConfig: EnvConfig? = null

    @Autowired
    var acsClient: IAcsClient? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun sendMailAsync(req: EmailRequest) {
        val logContext = IToLog {
            arrayOf<Any>(
                    "subject", req.getSubject(),
                    "to", req.getTo(),
                    "html_body", req.getHtmlBody()
            )
        }

        // In dev and uat - only send emails to @jskillcloud.com
        if (!EnvConstant.ENV_PROD.equals(envConfig.getName())) {
            // prepend env for sanity
            val subject = String.format("[%s] %s", envConfig.getName(), req.getSubject())
            req.setSubject(subject)
            if (!req.getTo().endsWith(MailConstant.STAFFJOY_EMAIL_SUFFIX)) {
                logger.warn("Intercepted sending due to non-production environment.")
                return
            }
        }
        val mailRequest = SingleSendMailRequest()
        mailRequest.setAccountName(MailConstant.FROM)
        mailRequest.setFromAlias(MailConstant.FROM_NAME)
        mailRequest.setAddressType(1)
        mailRequest.setToAddress(req.getTo())
        mailRequest.setReplyToAddress(false)
        mailRequest.setSubject(req.getSubject())
        mailRequest.setHtmlBody(req.getHtmlBody())
        try {
            val mailResponse: SingleSendMailResponse = acsClient.getAcsResponse(mailRequest)
            logger.info("Successfully sent email - request id : " + mailResponse.getRequestId(), logContext)
        } catch (ex: ClientException) {
            val sentryContext: Context = sentryClient.getContext()
            sentryContext.addTag("subject", req.getSubject())
            sentryContext.addTag("to", req.getTo())
            sentryClient.sendException(ex)
            logger.error("Unable to send email ", ex, logContext)
        }
    }

    companion object {
        private val logger: ILogger = SLoggerFactory.getLogger(MailSendService::class.java)
    }
}