package xyz.zelda.bot.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.account.dto.AccountDto

@Service
class GreetingService {
    @Autowired
    var helperService: HelperService? = null
    fun greeting(userId: String?) {
        val account: AccountDto = helperService!!.getAccountById(userId)
        val dispatchPreference = helperService!!.getPreferredDispatch(account)
        when (dispatchPreference) {
            DispatchPreference.DISPATCH_SMS -> helperService!!.smsGreetingAsync(account.getPhoneNumber())
            DispatchPreference.DISPATCH_EMAIL -> helperService!!.mailGreetingAsync(account)
            else -> logger.info("Unable to send greeting to user %s - no comm method found", userId)
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(GreetingService::class.java)
    }
}