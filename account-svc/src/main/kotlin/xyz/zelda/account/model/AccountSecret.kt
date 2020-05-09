package xyz.zelda.account.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account")
class AccountSecret {
    @Id
    private val id: String? = null
    private val email: String? = null
    private val confirmedAndActive = false
    private val passwordHash: String? = null
}