package xyz.zelda.account.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
class AccountSecret {
    @Id
    private val id: String? = null
    private val email: String? = null
    private val confirmedAndActive = false
    private val passwordHash: String? = null
}