package xyz.zelda.account.dto

import lombok.*
import xyz.zelda.infra.api.BaseResponse

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class GenericAccountResponse : BaseResponse() {
    private val account: AccountDto? = null
}