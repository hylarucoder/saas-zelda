package xyz.zelda.account.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import xyz.zelda.account.AccountConstant
import xyz.zelda.account.dto.*
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.validation.PhoneNumber
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@FeignClient(name = AccountConstant.SERVICE_NAME, path = "/v1/account", url = "\${zelda.account-service-endpoint}") // TODO Client side validation can be enabled as needed
interface AccountClient {
    @PostMapping(path = ["/create"])
    fun createAccount(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: CreateAccountRequest?): GenericAccountResponse?

    @PostMapping(path = ["/track_event"])
    fun trackEvent(@RequestBody @Valid request: TrackEventRequest?): BaseResponse?

    @PostMapping(path = ["/sync_user"])
    fun syncUser(@RequestBody @Valid request: SyncUserRequest?): BaseResponse?

    @GetMapping(path = ["/list"])
    fun listAccounts(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam offset: Int, @RequestParam @Min(0) limit: Int): ListAccountResponse?

    // GetOrCreate is for internal use by other APIs to match a user based on their phonenumber or email.
    @PostMapping(path = ["/get_or_create"])
    fun getOrCreateAccount(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: GetOrCreateRequest?): GenericAccountResponse?

    @GetMapping(path = ["/get"])
    fun getAccount(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam @NotBlank userId: String?): GenericAccountResponse?

    @PutMapping(path = ["/update"])
    fun updateAccount(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid newAccount: AccountDto?): GenericAccountResponse?

    @GetMapping(path = ["/get_account_by_phonenumber"])
    fun getAccountByPhonenumber(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam @PhoneNumber phoneNumber: String?): GenericAccountResponse?

    @PutMapping(path = ["/update_password"])
    fun updatePassword(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: UpdatePasswordRequest?): BaseResponse?

    @PostMapping(path = ["/verify_password"])
    fun verifyPassword(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: VerifyPasswordRequest?): GenericAccountResponse?

    // RequestPasswordReset sends an email to a user with a password reset link
    @PostMapping(path = ["/request_password_reset"])
    fun requestPasswordReset(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: PasswordResetRequest?): BaseResponse?

    @PostMapping(path = ["/request_email_change"])
    fun requestEmailChange(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: EmailChangeRequest?): BaseResponse?

    // ChangeEmail sets an account to active and updates its email. It is
    // used after a user clicks a confirmation link in their email.
    @PostMapping(path = ["/change_email"])
    fun changeEmail(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Valid request: EmailConfirmation?): BaseResponse?
}