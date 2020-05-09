package xyz.zelda.company.service

import org.springframework.stereotype.Service
import xyz.zelda.company.dto.TimeZoneList
import java.util.*

@Service
class TimeZoneService {
    fun listTimeZones(): TimeZoneList? {
        val timeZoneList: TimeZoneList = TimeZoneList.builder().build()
        for (id in TimeZone.getAvailableIDs()) {
            timeZoneList.getTimezones().add(id)
        }
        return timeZoneList
    }
}