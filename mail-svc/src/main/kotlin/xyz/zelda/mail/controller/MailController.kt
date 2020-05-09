package xyz.zelda.mail.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.mail.dto.EmailRequest
import xyz.zelda.mail.service.MailSendService
import javax.validation.Valid

@RestController
@RequestMapping("/v1")
@Validated
class MailController {
    @Autowired
    private val mailSendService: MailSendService? = null

    @PostMapping(path = "/send")
    fun send(@RequestBody @Valid request: EmailRequest): BaseResponse {
        mailSendService!!.sendMailAsync(request)
        return BaseResponse.builder().message("email has been sent async.").build()
    }

    companion object {
        private val logger: ILogger = SLoggerFactory.getLogger(MailController::class.java)
    }
}