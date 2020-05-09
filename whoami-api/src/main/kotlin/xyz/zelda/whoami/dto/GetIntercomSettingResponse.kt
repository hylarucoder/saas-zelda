package xyz.zelda.whoami.dto

import lombok.*
import xyz.zelda.infra.api.BaseResponse

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class GetIntercomSettingResponse : BaseResponse() {
    private val intercomSettings: IntercomSettingsDto? = null
}