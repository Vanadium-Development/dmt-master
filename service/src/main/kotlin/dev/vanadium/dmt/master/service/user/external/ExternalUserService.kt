package dev.vanadium.dmt.master.service.user.external

import org.keycloak.representations.UserInfo
import org.keycloak.representations.idm.UserRepresentation

interface ExternalUserService {

    fun fetchRoles(externalUserId: String): List<String>
    fun fetchUserInfo(externalUserId: String): UserRepresentation
}