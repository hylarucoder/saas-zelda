package xyz.staffjoy.common.auth

import lombok.Getter
import xyz.staffjoy.common.api.ResultCode

class PermissionDeniedException : RuntimeException {
    @Getter
    private val resultCode: ResultCode

    constructor(message: String?) : super(message) {
        resultCode = ResultCode.UN_AUTHORIZED
    }

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        this.resultCode = resultCode
    }

    constructor(resultCode: ResultCode, cause: Throwable?) : super(cause) {
        this.resultCode = resultCode
    }

    override fun fillInStackTrace(): Throwable {
        return this
    }
}