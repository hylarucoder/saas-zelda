package xyz.zelda.gateway.exceptions

class ForbiddenException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}