package xyz.zelda.bot.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zelda.account.dto.AccountDto;
import xyz.zelda.bot.dto.OnboardWorkerRequest;
import xyz.zelda.company.dto.CompanyDto;

@Service
public class OnBoardingService {

    static final ILogger logger = SLoggerFactory.getLogger(OnBoardingService.class);

    @Autowired
    HelperService helperService;

    public void onboardWorker(OnboardWorkerRequest req) {
        AccountDto account = helperService.getAccountById(req.getUserId());
        CompanyDto companyDto = helperService.getCompanyById(req.getCompanyId());

        DispatchPreference dispatchPreference = helperService.getPreferredDispatch(account);
        switch (dispatchPreference) {
            case DISPATCH_SMS:
                helperService.smsOnboardAsync(account, companyDto);
                break;
            case DISPATCH_EMAIL:
                helperService.mailOnBoardAsync(account, companyDto);
                break;
            default:
                logger.info("Unable to onboard user %s - no comm method found", req.getUserId());
        }
    }

}
