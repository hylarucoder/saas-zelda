package xyz.zelda.company.dto

import lombok.*
import xyz.zelda.infra.api.BaseResponse

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class GenericShiftListResponse : BaseResponse() {
    private val shiftList: ShiftList? = null
}