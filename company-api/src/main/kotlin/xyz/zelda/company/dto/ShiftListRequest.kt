package xyz.zelda.company.dto

import java.time.Instant
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ShiftListRequest {
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

    @AssertTrue(message = "shift_start_after must be before shift_start_before")
    private fun correctAfterAndBefore(): Boolean {
        val duration = shiftStartAfter!!.toEpochMilli() - shiftStartBefore!!.toEpochMilli()
        return duration < 0
    }
}