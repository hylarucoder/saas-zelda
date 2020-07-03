package xyz.zelda.sms.service

import com.aliyuncs.IAcsClient
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse
import com.aliyuncs.exceptions.ClientException
import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import io.sentry.context.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import xyz.zelda.sms.config.AppConfig
import xyz.zelda.sms.dto.SmsRequest
import xyz.zelda.sms.props.AppProps

@Service
class SmsSendService {
    @Autowired
    private val appProps: AppProps? = null

    @Autowired
    private val acsClient: IAcsClient? = null

    @Autowired
    var sentryClient: SentryClient? = null

    @Async(AppConfig.Companion.ASYNC_EXECUTOR_NAME)
    fun sendSmsAsync(smsRequest: SmsRequest) {
        val request = SendSmsRequest()
        request.setPhoneNumbers(smsRequest.getTo())
        request.setSignName(appProps.getAliyunSmsSignName())
        request.setTemplateCode(smsRequest.getTemplateCode())
        request.setTemplateParam(smsRequest.getTemplateParam())
        try {
            val response: SendSmsResponse = acsClient.getAcsResponse(request)
            if ("OK" == response.getCode()) {
                logger.info("SMS sent - " + response.getRequestId(),
                        "to", smsRequest.getTo(),
                        "template_code", smsRequest.getTemplateCode(),
                        "template_param", smsRequest.getTemplateParam())
            } else {
                val sentryContext: Context = sentryClient.getContext()
                sentryContext.addTag("to", smsRequest.getTo())
                sentryContext.addTag("template_code", smsRequest.getTemplateCode())
                sentryClient.sendMessage("bad aliyun sms response " + response.getCode())
                logger.error("failed to send: bad aliyun sms response " + response.getCode())
            }
        } catch (ex: ClientException) {
            val sentryContext: Context = sentryClient.getContext()
            sentryContext.addTag("to", smsRequest.getTo())
            sentryContext.addTag("template_code", smsRequest.getTemplateCode())
            sentryClient.sendException(ex)
            logger.error("failed to make aliyun sms request ", ex)
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(SmsSendService::class.java)
    }
}