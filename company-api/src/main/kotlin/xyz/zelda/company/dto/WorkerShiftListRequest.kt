package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class WorkerShiftListRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val workerId: String? = null

    @NotNull
    private val shiftStartAfter: Instant? = null

    @NotNull
    private val shiftStartBefore: Instant? = null

    @AssertTrue(message = "shift_start_after must be before shift_start_before")
    private fun correctAfterAndBefore(): Boolean {
        val duration = shiftStartAfter!!.toEpochMilli() - shiftStartBefore!!.toEpochMilli()
        return duration < 0
    }
}