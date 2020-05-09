package xyz.zelda.account.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AccountList {
    private val accounts: List<AccountDto>? = null
    private val limit = 0
    private val offset = 0
}