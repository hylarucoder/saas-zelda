package xyz.zelda.account.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import xyz.zelda.account.dto.*
import xyz.zelda.account.service.AccountService
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize
import xyz.zelda.infra.env.ENV
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.exception.PermissionDeniedException
import xyz.zelda.infra.exception.ServiceException
import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/v1/account")
@Validated
class AccountController {
    @Autowired
    private val accountService: AccountService? = null

    @Autowired
    private val envConfig: EnvConfig? = null

    // GetOrCreate is for internal use by other APIs to match a user based on their phonenumber or email.
    @PostMapping(path = "/get_or_create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_COMPANY_SERVICE])
    fun getOrCreate(@RequestBody @Valid request: GetOrCreateRequest): GenericAccountResponse {
        val accountDto: AccountDto = accountService!!.getOrCreate(request.getName(), request.getEmail(), request.getPhoneNumber())
        return GenericAccountResponse(accountDto)
    }

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_COMPANY_SERVICE])
    fun createAccount(@RequestBody @Valid request: CreateAccountRequest): GenericAccountResponse {
        val accountDto: AccountDto = accountService!!.create(request.getName(), request.getEmail(), request.getPhoneNumber())
        return GenericAccountResponse(accountDto)
    }

    @GetMapping(path = "/get_account_by_phonenumber")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_COMPANY_SERVICE])
    fun getAccountByPhonenumber(@RequestParam @PhoneNumber phoneNumber: String?): GenericAccountResponse {
        val accountDto: AccountDto = accountService!!.getAccountByPhoneNumber(phoneNumber)
        return GenericAccountResponse(accountDto)
    }

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listAccounts(@RequestParam offset: Int, @RequestParam @Min(0) limit: Int): ListAccountResponse {
        val accountList: AccountList = accountService!!.list(offset, limit)
        return ListAccountResponse(accountList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_COMPANY_SERVICE, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, AuthConstant.AUTHORIZATION_BOT_SERVICE, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE])
    fun getAccount(@RequestParam @NotBlank userId: String): GenericAccountResponse {
        validateAuthenticatedUser(userId)
        validateEnv()
        val accountDto: AccountDto = accountService!![userId]
        return GenericAccountResponse(accountDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_COMPANY_SERVICE, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE])
    fun updateAccount(@RequestBody @Valid newAccountDto: AccountDto): GenericAccountResponse {
        validateAuthenticatedUser(newAccountDto.getId())
        validateEnv()
        val accountDto: AccountDto = accountService!!.update(newAccountDto)
        return GenericAccountResponse(accountDto)
    }

    @PutMapping(path = "/update_password")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updatePassword(@RequestBody @Valid request: UpdatePasswordRequest): BaseResponse {
        validateAuthenticatedUser(request.getUserId())
        accountService!!.updatePassword(request.getUserId(), request.getPassword())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("password updated")
        return baseResponse
    }

    @PostMapping(path = "/verify_password")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun verifyPassword(@RequestBody @Valid request: VerifyPasswordRequest): GenericAccountResponse {
        val accountDto: AccountDto = accountService!!.verifyPassword(request.getEmail(), request.getPassword())
        return GenericAccountResponse(accountDto)
    }

    // RequestPasswordReset sends an email to a user with a password reset link
    @PostMapping(path = "/request_password_reset")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun requestPasswordReset(@RequestBody @Valid request: PasswordResetRequest): BaseResponse {
        accountService!!.requestPasswordReset(request.getEmail())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("password reset requested")
        return baseResponse
    }

    // RequestPasswordReset sends an email to a user with a password reset link
    @PostMapping(path = "/request_email_change")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun requestEmailChange(@RequestBody @Valid request: EmailChangeRequest): BaseResponse {
        validateAuthenticatedUser(request.getUserId())
        accountService!!.requestEmailChange(request.getUserId(), request.getEmail())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("email change requested")
        return baseResponse
    }

    // ChangeEmail sets an account to active and updates its email. It is
    // used after a user clicks a confirmation link in their email.
    @PostMapping(path = "/change_email")
    @Authorize(value = [AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun changeEmail(@RequestBody @Valid request: EmailConfirmation): BaseResponse {
        accountService!!.changeEmailAndActivateAccount(request.getUserId(), request.getEmail())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("email change requested")
        return baseResponse
    }

    @PostMapping(path = "/track_event")
    fun trackEvent(@RequestBody @Valid request: TrackEventRequest): BaseResponse {
        accountService!!.trackEvent(request.getUserId(), request.getEvent())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("event tracked")
        return baseResponse
    }

    @PostMapping(path = "/sync_user")
    fun syncUser(@RequestBody @Valid request: SyncUserRequest): BaseResponse {
        accountService!!.syncUser(request.getUserId())
        val baseResponse = BaseResponse()
        baseResponse.setMessage("user synced")
        return baseResponse
    }

    private fun validateAuthenticatedUser(userId: String) {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            val currentUserId: String = AuthContext.getUserId()
            if (StringUtils.isEmpty(currentUserId)) {
                throw ServiceException("failed to find current user id")
            }
            if (userId != currentUserId) {
                throw PermissionDeniedException("You do not have access to this service")
            }
        }
    }

    private fun validateEnv() {
        if (AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE.equals(AuthContext.getAuthz())) {
            if (!ENV.DEV == envConfig.name) {
                logger.warn("Development service trying to connect outside development environment")
                throw PermissionDeniedException("This service is not available outside development environments")
            }
        }
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(AccountController::class.java)
    }
}