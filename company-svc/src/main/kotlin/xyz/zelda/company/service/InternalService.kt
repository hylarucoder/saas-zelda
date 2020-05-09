package xyz.zelda.company.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.company.dto.GrowthGraphResponse
import xyz.zelda.company.repo.ShiftRepo
import java.util.*

@Service
class InternalService {
    @Autowired
    var shiftRepo: ShiftRepo? = null

    // PeopleOnShifts returns the count of people working right now
    val growthGraph: GrowthGraphResponse

    // ScheduledPerWeek returns the weekly number of shifts/week
    ?
        get() {
            // PeopleOnShifts returns the count of people working right now
            val peopleOnShifts = shiftRepo.getPeopleOnShifts()

            // ScheduledPerWeek returns the weekly number of shifts/week
            val scheduledPerWeekList = shiftRepo.getScheduledPerWeekList()
            val stuff: MutableMap<String?, Int?> = HashMap()
            for (scheduledPerWeek in scheduledPerWeekList!!) {
                stuff[scheduledPerWeek.week] = scheduledPerWeek.count
            }
            return GrowthGraphResponse.builder()
                    .peopleScheduledPerWeek(stuff)
                    .peopleOnShift(peopleOnShifts)
                    .build()
        }
}