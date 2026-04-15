package moa.moabackend.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    var secret: String = ""
    var accessExp: Long = 0
    var refreshExp: Long = 0
    var header: String = ""
    var prefix: String = ""
}
