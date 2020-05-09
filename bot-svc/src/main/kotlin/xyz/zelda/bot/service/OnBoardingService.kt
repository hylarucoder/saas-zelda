package xyz.zelda.bot.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.bot.dto.OnboardWorkerRequest

@Service
class OnBoardingService {
    @Autowired
    var helperService: HelperService? = null
    fun onboardWorker(req: OnboardWorkerRequest) {
        val account = helperService!!.getAccountById(req.getUserId())
        val companyDto = helperService!!.getCompanyById(req.getCompanyId())
        val dispatchPreference = helperService!!.getPreferredDispatch(account)
        when (dispatchPreference) {
            DispatchPreference.DISPATCH_SMS -> helperService!!.smsOnboardAsync(account, companyDto)
            DispatchPreference.DISPATCH_EMAIL -> helperService!!.mailOnBoardAsync(account, companyDto)
            else -> logger.info("Unable to onboard user %s - no comm method found", req.getUserId())
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(OnBoardingService::class.java)
    }
}