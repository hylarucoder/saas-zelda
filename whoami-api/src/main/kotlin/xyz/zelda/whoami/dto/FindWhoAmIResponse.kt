package xyz.zelda.whoami.dto

import lombok.*
import xyz.zelda.infra.api.BaseResponse

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class FindWhoAmIResponse : BaseResponse() {
    private val iAm: IAmDto? = null
}