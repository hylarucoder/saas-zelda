package xyz.zelda.account.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
class Account {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private val id: String? = null
    private val name: String? = null
    private val email: String? = null
    private val confirmedAndActive = false
    private val memberSince: Instant? = null
    private val support = false
    private val phoneNumber: String? = null
    private val photoUrl: String? = null
}