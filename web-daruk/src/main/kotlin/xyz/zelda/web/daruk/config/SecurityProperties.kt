package xyz.zelda.web.daruk.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt-security")
class SecurityProperties {
  var secret = ""
  var expirationTime: Int = 31 // in days
  var tokenPrefix = "Bearer "
  var headerString = "Authorization"
  var strength = 10
}