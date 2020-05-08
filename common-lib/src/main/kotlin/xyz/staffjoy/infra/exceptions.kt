package xyz.staffjoy.common

import lombok.Getter

/**
 * Business Service Exception
 */
class ServiceException : RuntimeException {

    private val resultCode: StatusCode

    constructor(resultCode: StatusCode, msg: String?) : super(msg) {
        this.resultCode = resultCode
    }

    /**
     * for better performance
     *
     * @return Throwable
     */
    override fun fillInStackTrace(): Throwable {
        return this
    }

    fun doFillInStackTrace(): Throwable {
        return super.fillInStackTrace()
    }
}

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