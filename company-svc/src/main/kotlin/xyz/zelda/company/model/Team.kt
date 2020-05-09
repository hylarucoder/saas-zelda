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
class Team {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private val id: String? = null
    private val companyId: String? = null
    private val name: String? = null
    private val archived = false
    private val timezone: String? = null
    private val dayWeekStarts: String? = null
    private val color: String? = null
}