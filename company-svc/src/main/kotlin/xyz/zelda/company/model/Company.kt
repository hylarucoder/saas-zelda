package xyz.zelda.company.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.GenericGenerator
import xyz.zelda.infra.validation.DayOfWeek
import xyz.zelda.infra.validation.Group1
import xyz.zelda.infra.validation.Timezone
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
class Company {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private val id: String? = null
    private val name: String? = null
    private val archived = false
    private val defaultTimezone: String? = null
    private val defaultDayWeekStarts: String? = null
}