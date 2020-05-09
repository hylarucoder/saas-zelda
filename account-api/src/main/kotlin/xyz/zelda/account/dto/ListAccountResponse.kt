package xyz.zelda.account.dto

import lombok.*
import xyz.zelda.infra.api.BaseResponse

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class ListAccountResponse : BaseResponse() {
    private val accountList: AccountList? = null
}