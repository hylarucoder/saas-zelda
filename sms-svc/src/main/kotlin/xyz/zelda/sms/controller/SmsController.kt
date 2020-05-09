package xyz.zelda.sms.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.Authorize
import xyz.zelda.sms.dto.SmsRequest
import xyz.zelda.sms.props.AppProps
import xyz.zelda.sms.service.SmsSendService
import javax.validation.Valid

@RestController
@RequestMapping("/v1")
@Validated
class SmsController {
    @Autowired
    private val appProps: AppProps? = null

    @Autowired
    private val smsSendService: SmsSendService? = null

    @PostMapping(path = "/queue_send")
    @Authorize([AuthConstant.AUTHORIZATION_COMPANY_SERVICE, AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_BOT_SERVICE])
    fun send(@RequestBody @Valid smsRequest: SmsRequest): BaseResponse {
        if (appProps.isWhiteListOnly()) {
            val whiteList: String = appProps.getWhiteListPhoneNumbers()
            val allowedToSend = (!StringUtils.isEmpty(whiteList)
                    && whiteList.contains(smsRequest.getTo()))
            if (!allowedToSend) {
                val msg = java.lang.String.format("prevented sending to number %s due to whitelist", smsRequest.getTo())
                logger.warn(msg)
                return BaseResponse.builder().code(ResultCode.REQ_REJECT).message(msg).build()
            }
        }
        smsSendService!!.sendSmsAsync(smsRequest)
        val msg = java.lang.String.format("sent message to %s. async", smsRequest.getTo())
        logger.debug(msg)
        return BaseResponse.builder().message(msg).build()
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(SmsController::class.java)
    }
}