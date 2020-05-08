package xyz.zelda.infra.services

object SecurityConstant {
    // Public security means a user may be logged out or logged in
    const val SEC_PUBLIC = 0

    // Authenticated security means a user must be logged in
    const val SEC_AUTHENTICATED = 1

    // Admin security means a user must be both logged in and have sudo flag
    const val SEC_ADMIN = 2
}