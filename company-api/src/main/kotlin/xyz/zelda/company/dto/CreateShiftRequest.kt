package xyz.zelda.company.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class CreateShiftRequest {
    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotNull
    private val start: Instant? = null

    @NotNull
    private val stop: Instant? = null

    @Builder.Default
    private val userId = ""

    @Builder.Default
    private val jobId = ""

    @NotNull
    private val published = false

    @AssertTrue(message = "stop must be after start")
    private fun shopIsAfterStart(): Boolean {
        val duration = stop!!.toEpochMilli() - start!!.toEpochMilli()
        return duration > 0
    }

    @AssertTrue(message = "Shifts exceed max allowed hour duration")
    private fun withInMaxDuration(): Boolean {
        val duration = stop!!.toEpochMilli() - start!!.toEpochMilli()
        return duration <= MAX_SHIFT_DURATION
    }

    companion object {
        val MAX_SHIFT_DURATION = TimeUnit.HOURS.toMillis(23)
    }
}