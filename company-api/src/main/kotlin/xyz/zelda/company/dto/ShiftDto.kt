package xyz.zelda.company.dto

import java.time.Instant
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ShiftDto {
    @NotBlank
    private val id: String? = null

    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotNull
    private val start: Instant? = null

    @NotNull
    private val stop: Instant? = null
    private val userId: String? = null
    private val jobId: String? = null

    @NotNull
    private val published = false

    @AssertTrue(message = "stop must be after start")
    private fun stopIsAfterStart(): Boolean {
        val duration = stop!!.toEpochMilli() - start!!.toEpochMilli()
        return duration > 0
    }

    @AssertTrue(message = "Shifts exceed max allowed hour duration")
    private fun withInMaxDuration(): Boolean {
        val duration = stop!!.toEpochMilli() - start!!.toEpochMilli()
        return duration <= CreateShiftRequest.Companion.MAX_SHIFT_DURATION
    }
}