package xyz.zelda.company.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
class Worker {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private val id: String? = null
    private val teamId: String? = null
    private val userId: String? = null
}