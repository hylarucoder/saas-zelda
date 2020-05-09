package xyz.zelda.whoami.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize
import xyz.zelda.whoami.dto.FindWhoAmIResponse
import xyz.zelda.whoami.dto.GetIntercomSettingResponse
import xyz.zelda.whoami.dto.IAmDto
import xyz.zelda.whoami.dto.IntercomSettingsDto
import xyz.zelda.whoami.service.WhoAmIService

@RestController
@RequestMapping("/v1")
class WhoAmIController {
    @Autowired
    var whoAmIService: WhoAmIService? = null

    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    @GetMapping
    fun findWhoAmI(): FindWhoAmIResponse {
        val userId: String = AuthContext.getUserId()
        val iAmDto: IAmDto? = whoAmIService!!.findWhoIAm(userId)
        val authz: String = AuthContext.getAuthz()
        if (AuthConstant.AUTHORIZATION_SUPPORT_USER.equals(authz)) {
            iAmDto.setSupport(true)
        }
        return FindWhoAmIResponse(iAmDto)
    }

    @get:GetMapping(value = "/intercom")
    @get:Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    val intercomSettings: GetIntercomSettingResponse
        get() {
            val userId: String = AuthContext.getUserId()
            val intercomSettingsDto: IntercomSettingsDto? = whoAmIService!!.findIntercomSettings(userId)
            return GetIntercomSettingResponse(intercomSettingsDto)
        }
}