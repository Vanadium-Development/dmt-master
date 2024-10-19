package dev.vanadium.dmt.master.authentication.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "vanadium.dmt.authentication")
class AuthenticationProperties {

    lateinit var jwtSecret: String

    lateinit var accessTokenExpiration: Duration

    lateinit var refreshTokenExpiration: Duration

    lateinit var postAuthenticationRedirect: String

    var jwtIssuer: String = "dmt.vanadium.dev"
    var jwtAudiencePrefix: String = "dmt.vanadium.dev/"
}