package dev.vanadium.dmt.master.service.user.external

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanadium.dmt.master.integration.keycloak.annotation.IdentityClientId
import dev.vanadium.dmt.master.integration.keycloak.annotation.Realm
import dev.vanadium.dmt.master.commons.rbac.UserRole
import jakarta.annotation.PostConstruct
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeycloakUserService : ExternalUserService {

    @Autowired
    private lateinit var keycloak: Keycloak

    @Autowired
    @Realm
    private lateinit var realm: String

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun migrateRoles() {
        UserRole.entries.forEach {
            if(!keycloak.realm(realm).clients().get(identityClientId).roles().list().any { role -> role.name == it.roleName }) {
                keycloak.realm(realm).clients().get(identityClientId).roles().create(RoleRepresentation(
                    it.roleName, it.description, false
                ))

                logger.info("Created non-existing role in KeyCloak: $it (${it.roleName})")
            }
        }
    }

    @Autowired
    @IdentityClientId
    private lateinit var identityClientId: String
    override fun fetchRoles(externalUserId: String): List<String> {
        return keycloak.realms().realm(realm).users()[externalUserId].roles().clientLevel(identityClientId).listAll().map { it.name }
    }

    override fun fetchUserInfo(externalUserId: String): UserRepresentation {
        // TODO: Implement caching mechanism
        val isCached = false

        val response = keycloak.realms().realm(realm)
            .users()[externalUserId]
            .toRepresentation(true)

        logger.info("Fetched user information from KeyCloak (cached=$isCached) ${objectMapper.writeValueAsString(response)}")

        return response
    }

}