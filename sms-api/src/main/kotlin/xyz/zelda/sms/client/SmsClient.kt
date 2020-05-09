package xyz.zelda.sms.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.sms.SmsConstant
import xyz.zelda.sms.dto.SmsRequest
import javax.validation.Valid

@FeignClient(name = SmsConstant.SERVICE_NAME, path = "/v1", url = "\${zelda.sms-service-endpoint}")
interface SmsClient {
    @PostMapping(path = "/queue_send")
    fun send(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid smsRequest: SmsRequest?): BaseResponse?
}