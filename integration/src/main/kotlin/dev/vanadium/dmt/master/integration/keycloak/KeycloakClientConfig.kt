package dev.vanadium.dmt.master.integration.keycloak

import dev.vanadium.dmt.master.integration.keycloak.annotation.IdentityClientId
import dev.vanadium.dmt.master.integration.keycloak.annotation.Realm
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakClientConfig {

    @Autowired
    private lateinit var keycloakClientProperties: KeycloakClientProperties

    @Bean
    fun keycloakClient(): Keycloak {
        return KeycloakBuilder
            .builder()
            .serverUrl(keycloakClientProperties.serverUrl)
            .realm(keycloakClientProperties.realm)
            .clientId(keycloakClientProperties.clientId)
            .grantType(OAuth2Constants.PASSWORD)
            .username(keycloakClientProperties.username)
            .password(keycloakClientProperties.password)
            .build()
    }

    @Bean
    @Realm
    fun kayCloakRealm(): String {
        return keycloakClientProperties.realm
    }

    @Bean
    @IdentityClientId
    fun identityClientId(): String {
        return keycloakClientProperties.dmtIdentityClientId
    }
}