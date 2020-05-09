package xyz.zelda.company.dto

import java.time.Instant
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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