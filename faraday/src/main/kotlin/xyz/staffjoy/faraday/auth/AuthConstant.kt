package xyz.staffjoy.faraday.auth

object AuthConstant {
    const val COOKIE_NAME = "staffjoy-faraday"

    // header set for internal user id
    const val CURRENT_USER_HEADER = "faraday-current-user-id"

    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    const val AUTHORIZATION_HEADER = "Authorization"

    // AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
    // a request is being made bu an unauthenticated web user
    const val AUTHORIZATION_ANONYMOUS_WEB = "faraday-anonymous"

    // AUTHORIZATION_COMPANY_SERVICE is set as the Authorization header to denote
    // that a request is being made by the company service
    const val AUTHORIZATION_COMPANY_SERVICE = "company-service"

    // AUTHORIZATION_BOT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the bot microservice
    const val AUTHORIZATION_BOT_SERVICE = "bot-service"

    // AUTHORIZATION_ACCOUNT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the account service
    const val AUTHORIZATION_ACCOUNT_SERVICE = "account-service"

    // AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
    // a request is being made by a Staffjoy team member
    const val AUTHORIZATION_SUPPORT_USER = "faraday-support"

    // AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
    // denote that a request is being made by the dev-only superpowers service
    const val AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service"

    // AUTHORIZATION_WWW_SERVICE is set as the Authorization header to denote that
    // a request is being made by the www login / signup system
    const val AUTHORIZATION_WWW_SERVICE = "www-service"

    // AUTH_WHOAMI_SERVICE is set as the Authorization heade to denote that
    // a request is being made by the whoami microservice
    const val AUTHORIZATION_WHOAMI_SERVICE = "whoami-service"

    // AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
    // a request is being made by an authenticated we6b user
    const val AUTHORIZATION_AUTHENTICATED_USER = "faraday-authenticated"

    // AUTHORIZATION_ICAL_SERVICE is set as the Authorization header to denote that
    // a request is being made by the ical service
    const val AUTHORIZATION_ICAL_SERVICE = "ical-service"

    // AUTH ERROR Messages
    const val ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service"
    const val ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header"
}