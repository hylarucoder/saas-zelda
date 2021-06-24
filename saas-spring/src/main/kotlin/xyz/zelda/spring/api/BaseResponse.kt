package xyz.zelda.spring.api

data class BaseResponse(
        val status: ResultCode = ResultCode.SUCCESS,
        val message: String? = null,
        val code: ResultCode = ResultCode.SUCCESS
)
