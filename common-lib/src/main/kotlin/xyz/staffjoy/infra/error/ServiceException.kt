package xyz.staffjoy.common.error

import lombok.Getter
import xyz.staffjoy.common.api.ResultCode

/**
 * Business Service Exception
 *
 * @author william
 */
class ServiceException : RuntimeException {
    @Getter
    private val resultCode: ResultCode

    constructor(message: String?) : super(message) {
        resultCode = ResultCode.FAILURE
    }

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        this.resultCode = resultCode
    }

    constructor(resultCode: ResultCode, msg: String?) : super(msg) {
        this.resultCode = resultCode
    }

    constructor(resultCode: ResultCode, cause: Throwable?) : super(cause) {
        this.resultCode = resultCode
    }

    constructor(msg: String?, cause: Throwable?) : super(msg, cause) {
        resultCode = ResultCode.FAILURE
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

    companion object {
        private const val serialVersionUID = 2359767895161832954L
    }
}