package xyz.zelda.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import xyz.zelda.infra.auth.Sessions
import xyz.zelda.infra.env.EnvConfig
import javax.servlet.http.HttpServletResponse

@Controller
class LogoutController {
    @Autowired
    private val envConfig: EnvConfig? = null

    @RequestMapping(value = "/logout")
    fun logout(response: HttpServletResponse?): String {
        Sessions.logout(envConfig.getExternalApex(), response)
        return "redirect:/"
    }
}