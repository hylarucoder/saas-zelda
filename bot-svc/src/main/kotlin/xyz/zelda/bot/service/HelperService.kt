package xyz.zelda.bot.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.bot.BotConstant
import xyz.zelda.bot.config.AppConfig
import xyz.zelda.bot.props.AppProps
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.company.dto.CompanyDto
import xyz.zelda.company.dto.GenericCompanyResponse
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.exception.ServiceException
import xyz.zelda.mail.client.MailClient
import xyz.zelda.mail.dto.EmailRequest
import xyz.zelda.sms.client.SmsClient
import xyz.zelda.sms.dto.SmsRequest
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.json.Json

@Component
class HelperService {
    @Autowired
    var envConfig: EnvConfig? = null

    @Autowired
    var smsClient: SmsClient? = null

    @Autowired
    var mailClient: MailClient? = null

    @Autowired
    var accountClient: AccountClient? = null

    @Autowired
    var companyClient: CompanyClient? = null

    @Autowired
    var appProps: AppProps? = null

    @Autowired
    private val sentryClient: SentryClient? = null
    fun getPreferredDispatch(account: AccountDto): DispatchPreference {
        if (appProps.isForceEmailPreference()) {
            return DispatchPreference.DISPATCH_EMAIL
        }
        // todo - check user notification preferences
        if (!StringUtils.isEmpty(account.getPhoneNumber())) {
            return DispatchPreference.DISPATCH_SMS
        }
        return if (!StringUtils.isEmpty(account.getEmail())) {
            DispatchPreference.DISPATCH_EMAIL
        } else DispatchPreference.DISPATCH_UNAVAILABLE
    }

    fun getAccountById(userId: String?): AccountDto {
        var resp: GenericAccountResponse? = null
        resp = try {
            accountClient.getAccount(AuthConstant.AUTHORIZATION_BOT_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "fail to get user"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!resp.isSuccess()) {
            logger.error(resp.getMessage())
            sentryClient.sendMessage(resp.getMessage())
            throw ServiceException(resp.getMessage())
        }
        return resp.getAccount()
    }

    fun getCompanyById(companyId: String?): CompanyDto {
        var response: GenericCompanyResponse? = null
        response = try {
            companyClient.getCompany(AuthConstant.AUTHORIZATION_BOT_SERVICE, companyId)
        } catch (ex: Exception) {
            val errMsg = "fail to get company"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!response.isSuccess()) {
            logger.error(response.getMessage())
            sentryClient.sendMessage(response.getMessage())
            throw ServiceException(response.getMessage())
        }
        return response.getCompany()
    }

    fun sendMail(email: String?, name: String?, subject: String?, htmlBody: String?) {
        val emailRequest: EmailRequest = EmailRequest.builder()
                .to(email)
                .name(name)
                .subject(subject)
                .htmlBody(htmlBody)
                .build()
        var baseResponse: BaseResponse? = null
        baseResponse = try {
            mailClient.send(emailRequest)
        } catch (ex: Exception) {
            val errMsg = "Unable to send email"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!baseResponse.isSuccess()) {
            logger.error(baseResponse.getMessage())
            sentryClient.sendMessage(baseResponse.getMessage())
            throw ServiceException(baseResponse.getMessage())
        }
    }

    fun sendSms(phoneNumber: String?, templateCode: String?, templateParam: String?) {
        val smsRequest: SmsRequest = SmsRequest.builder()
                .to(phoneNumber)
                .templateCode(templateCode)
                .templateParam(templateParam)
                .build()
        var baseResponse: BaseResponse? = null
        baseResponse = try {
            smsClient.send(AuthConstant.AUTHORIZATION_BOT_SERVICE, smsRequest)
        } catch (ex: Exception) {
            val errMsg = "could not send sms"
            logger.error(errMsg, ex)
            sentryClient.sendException(ex)
            throw ServiceException(errMsg, ex)
        }
        if (!baseResponse.isSuccess()) {
            logger.error(baseResponse.getMessage())
            sentryClient.sendMessage(baseResponse.getMessage())
            throw ServiceException(baseResponse.getMessage())
        }
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun smsGreetingAsync(phoneNumber: String?) {
        val templateCode: String = BotConstant.GREETING_SMS_TEMPLATE_CODE
        val templateParam = ""
        sendSms(phoneNumber, templateCode, templateParam)
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun mailGreetingAsync(accountDto: AccountDto) {
        val email: String = accountDto.getEmail()
        val name: String = accountDto.getName()
        val subject = "Staffjoy Greeting"
        val htmlBody: String = BotConstant.GREETING_EMAIL_TEMPLATE
        sendMail(email, name, subject, htmlBody)
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun mailOnBoardAsync(account: AccountDto, companyDto: CompanyDto) {
        var icalURI: URI? = null
        icalURI = try {
            URI(envConfig.getScheme(), "ical." + envConfig.getExternalApex(), java.lang.String.format("/%s.ics", account.getId()), null)
        } catch (e: URISyntaxException) {
            throw ServiceException("Fail to build URI", e)
        }
        val greet = getGreet(getFirstName(account.getName()))
        val companyName: String = companyDto.getName()
        val icalUrl = icalURI.toString()
        val email: String = account.getEmail()
        val name: String = account.getName()
        val htmlBody: String = java.lang.String.format(BotConstant.ONBOARDING_EMAIL_TEMPLATE, greet, companyName, icalUrl)
        val subject = "Onboarding Message"
        sendMail(email, name, subject, htmlBody)

        // todo - check if upcoming shifts, and if there are - send them
        logger.info(java.lang.String.format("onboarded worker %s (%s) for company %s (%s)", account.getId(), account.getName(), companyDto.getId(), companyDto.getName()))
    }

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    fun smsOnboardAsync(account: AccountDto, companyDto: CompanyDto) {
        var icalURI: URI? = null
        icalURI = try {
            URI(envConfig.getScheme(), "ical." + envConfig.getExternalApex(), java.lang.String.format("/%s.ics", account.getId()), null)
        } catch (e: URISyntaxException) {
            throw ServiceException("Fail to build URI", e)
        }
        val templateParam1: String = Json.createObjectBuilder()
                .add("greet", getGreet(getFirstName(account.getName())))
                .add("company_name", companyDto.getName())
                .build()
                .toString()
        val templateParam3: String = Json.createObjectBuilder()
                .add("ical_url", icalURI.toString())
                .build()
                .toString()

        // TODO crate sms template on aliyun then update code
//        String[] onboardingMessages = {
//                String.format("%s Your manager just added you to %s on Staffjoy to share your work schedule.", HelperService.getGreet(HelperService.getFirstName(account.getName())), companyDto.getName()),
//                "When your manager publishes your shifts, we'll send them to you here. (To disable Staffjoy messages, reply STOP at any time)",
//                String.format("Click this link to sync your shifts to your phone's calendar app: %s", icalURI.toString())
//        };
        val onboardingMessageMap: Map<String, String> = object : HashMap<String?, String?>() {
            init {
                put(BotConstant.ONBOARDING_SMS_TEMPLATE_CODE_1, templateParam1)
                put(BotConstant.ONBOARDING_SMS_TEMPLATE_CODE_2, "")
                put(BotConstant.ONBOARDING_SMS_TEMPLATE_CODE_3, templateParam3)
            }
        }
        for ((templateCode, templateParam) in onboardingMessageMap) {
            sendSms(account.getPhoneNumber(), templateCode, templateParam)
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(4))
            } catch (e: InterruptedException) {
                logger.warn("InterruptedException", e)
            }
        }
        // todo - check if upcoming shifts, and if there are - send them
        logger.info(java.lang.String.format("onboarded worker %s (%s) for company %s (%s)", account.getId(), account.getName(), companyDto.getId(), companyDto.getName()))
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(HelperService::class.java)
        val standardGreetings = arrayOf(
                "Hi %s!",
                "Hey %s -",
                "Hello %s.",
                "Hey, %s!"
        )

        fun getGreet(firstName: String?): String {
            return String.format(standardGreetings[Random().nextInt(standardGreetings.size)], firstName)
        }

        fun getFirstName(name: String): String {
            if (StringUtils.isEmpty(name)) return "there"
            val names = name.split(" ".toRegex()).toTypedArray()
            return names[0]
        }
    }
}