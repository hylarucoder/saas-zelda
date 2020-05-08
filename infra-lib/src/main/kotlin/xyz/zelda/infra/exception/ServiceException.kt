package xyz.zelda.infra.exception

import xyz.zelda.infra.api.ResultCode

class ServiceException(message: String?, resultCode: ResultCode = ResultCode.FAILURE) : RuntimeException(message) {
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