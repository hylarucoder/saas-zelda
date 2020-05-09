package xyz.zelda.ical.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import xyz.zelda.ical.service.ICalService
import java.nio.charset.Charset

@Controller
class ICalController {
    @Autowired
    private val iCalService: ICalService? = null

    @GetMapping(value = "/{user_id}.ics")
    @ResponseBody
    fun getCalByUserId(@PathVariable(value = "user_id") userId: String): HttpEntity<ByteArray> {
        val cal = iCalService!!.getCalByUserId(userId)
        val calBytes = cal!!.build().toByteArray()
        val header = HttpHeaders()
        header.setContentType(MediaType("application", "calendar", Charset.forName("UTF-8")))
        header.set("Content-Disposition", "attachment; filename=$userId.ics")
        header.setContentLength(calBytes.size)
        return HttpEntity(calBytes, header)
    }
}