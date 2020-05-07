package xyz.staffjoy.faraday.api

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class BaseResponse {
    private val message: String? = null

    @Builder.Default
    private val code = ResultCode.SUCCESS
    val isSuccess: Boolean
        get() = code == ResultCode.SUCCESS
}