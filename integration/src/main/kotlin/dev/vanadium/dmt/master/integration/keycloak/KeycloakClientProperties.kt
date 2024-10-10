package dev.vanadium.dmt.master.integration.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vanadium.dmt.integration.keycloak")
class KeycloakClientProperties {

    var realm: String = "dmt"
    var clientId: String = "admin-cli"

    lateinit var serverUrl: String
    lateinit var username: String
    lateinit var password: String
    lateinit var dmtIdentityClientId: String
}