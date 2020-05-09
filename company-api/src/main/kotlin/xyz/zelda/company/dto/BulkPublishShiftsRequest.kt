package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class BulkPublishShiftsRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null
    private val userId: String? = null
    private val jobId: String? = null

    @NotNull
    private val shiftStartAfter: Instant? = null

    @NotNull
    private val shiftStartBefore: Instant? = null
    private val published = false
}