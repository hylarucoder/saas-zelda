package xyz.staffjoy.common



enum class StatusCode(val status: Int, val message: String) {
    SUCCESS(200, "SUCCESS"),
    API_EXCEPTION(400, "API_EXCEPTION"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    PERMISSION_DENIED(403, "PERMISSION_DENIED"),
    NOT_FOUND(404, "NOT_FOUND")
}

enum class BizCode(val code: Int, val status: Int, val message: String) {
    SUCCESS(100200, StatusCode.SUCCESS.status, StatusCode.SUCCESS.message),
    API_EXCEPTION(100400, StatusCode.API_EXCEPTION.status, StatusCode.API_EXCEPTION.message),
    UNAUTHORIZED(100401, StatusCode.UNAUTHORIZED.status, StatusCode.UNAUTHORIZED.message),
    PERMISSION_DENIED(100403, StatusCode.PERMISSION_DENIED.status, StatusCode.NOT_FOUND.message),
    NOT_FOUND(100404, StatusCode.NOT_FOUND.status, StatusCode.NOT_FOUND.message)
}

class BaseResponse {
    private val message: String? = null

    private val code = StatusCode.SUCCESS
    val isSuccess: Boolean
        get() = code == StatusCode.SUCCESS
}