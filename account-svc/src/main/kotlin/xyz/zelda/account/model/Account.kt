package xyz.zelda.account.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import java.time.Instant

@Entity
class Account(
        @Id
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @GeneratedValue(generator = "system-uuid")
        val id: String? = null,
        val name: String? = null,
        val email: String? = null,
        val confirmedAndActive: Boolean = false,
        val memberSince: Instant? = null,
        val support: Boolean = false,
        val phoneNumber: String? = null,
        val photoUrl: String? = null
)
