package xyz.zelda.whoami.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.whoami.WhoAmIConstant
import xyz.zelda.whoami.dto.FindWhoAmIResponse
import xyz.zelda.whoami.dto.GetIntercomSettingResponse

@FeignClient(name = WhoAmIConstant.SERVICE_NAME, path = "/v1", url = "\${zelda.whoami-service-endpoint}")
interface WhoAmIClient {
    @GetMapping
    fun findWhoAmI(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?): FindWhoAmIResponse?

    @GetMapping(value = "/intercom")
    fun getIntercomSettings(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?): GetIntercomSettingResponse?
}