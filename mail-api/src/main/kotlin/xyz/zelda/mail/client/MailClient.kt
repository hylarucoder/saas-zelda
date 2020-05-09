package xyz.zelda.mail.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.mail.MailConstant
import xyz.zelda.mail.dto.EmailRequest
import javax.validation.Valid

@FeignClient(name = MailConstant.SERVICE_NAME, path = "/v1", url = "\${zelda.email-service-endpoint}")
interface MailClient {
    @PostMapping(path = "/send")
    fun send(@RequestBody @Valid request: EmailRequest?): BaseResponse?
}