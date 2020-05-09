package xyz.zelda.company.dto

import java.time.Instant
import java.util.*

class ShiftList {
    private val shifts: List<ShiftDto> = ArrayList()
    private val shiftStartAfter: Instant? = null
    private val shiftStartBefore: Instant? = null
}