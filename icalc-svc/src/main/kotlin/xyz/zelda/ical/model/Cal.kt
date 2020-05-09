package xyz.zelda.ical.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.company.dto.ShiftDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Cal {
    private val companyName: String? = null
    private val shiftList: List<ShiftDto>? = null
    private fun getCalDateFormat(dt: Instant): String {
        return DATE_TIME_FORMATTER.format(dt)
    }

    val header: String
        get() {
            val header = StringBuilder()
            header.append("BEGIN:VCALENDAR\r\n")
            header.append("METHOD:PUBLISH\r\n")
            header.append("VERSION:2.0\r\n")
            header.append("PRODID:-//Staffjoy//Staffjoy ICal Service//EN\r\n")
            return header.toString()
        }

    val body: String
        get() {
            val body = StringBuilder()
            for (shiftDto in shiftList!!) {
                body.append("BEGIN:VEVENT\r\n")
                body.append("ORGANIZER;CN=Engineering:MAILTO:support@zelda.xyz\r\n")
                body.append("""
    SUMMARY: Work at ${companyName}
    
    """.trimIndent())
                body.append("""
    UID:${shiftDto.getUserId().toString()}
    
    """.trimIndent())
                body.append("STATUS:CONFIRMED\r\n")
                body.append("""
    DTSTART:${getCalDateFormat(shiftDto.getStart())}
    
    """.trimIndent())
                body.append("""
    DTEND:${getCalDateFormat(shiftDto.getStop())}
    
    """.trimIndent())
                body.append("""
    DTSTAMP:${getCalDateFormat(Instant.now())}
    
    """.trimIndent())
                body.append("""
    LAST-MODIFIED:${getCalDateFormat(Instant.now())}
    
    """.trimIndent())
                body.append("""
    LOCATION:  ${companyName}
    
    """.trimIndent())
                body.append("END:VEVENT\r\n")
            }
            return body.toString()
        }

    val footer: String
        get() = "END:VCALENDAR"

    // Build concats an ical header/body/footer together
    fun build(): String {
        return header + body + footer
    }

    companion object {
        const val CAL_DATE_PATTERN = "yyyyMMdd'T'HHmmssZ"
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CAL_DATE_PATTERN)
                .withZone(ZoneId.systemDefault())
    }
}