package xyz.staffjoy.faraday.exceptions

class FaradayException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}