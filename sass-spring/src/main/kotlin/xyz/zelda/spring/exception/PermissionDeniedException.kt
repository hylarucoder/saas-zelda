package xyz.zelda.spring.exception

import xyz.zelda.spring.api.ResultCode

class PermissionDeniedException(message: String?, resultCode: ResultCode = ResultCode.FAILURE) : RuntimeException(message) {
    val status = resultCode

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