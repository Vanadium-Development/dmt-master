package dev.vanadium.dmt.master.authentication.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "vanadium.dmt.authentication")
class AuthenticationProperties {

    lateinit var jwtSecret: String

    lateinit var accessTokenExpiration: Duration

    lateinit var refreshTokenExpiration: Duration
}