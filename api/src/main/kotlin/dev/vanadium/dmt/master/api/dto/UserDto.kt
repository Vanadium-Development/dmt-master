package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.domainmodel.user.User
import java.util.UUID

data class UserDto(
    val id: UUID,
    val externalId: String,
    val userInfo: UserInfo
)




fun User.toDto(): UserDto {
    return UserDto(this.id, this.externalId, UserInfo(this.firstName, this.lastName, this.username))
}

data class UserContextDto(
    val user: UserDto,
    val roles: List<String>,
    val userInfo: UserInfo
)

data class UserInfo(
    val firstName: String?,
    val lastName: String?,
    val username: String?
)

fun UserContext.UserInfo.toDto(): UserInfo {
    return UserInfo(this.firstName, this.lastName, this.username)
}

fun UserContext.toDto(): UserContextDto {
    return UserContextDto(this.tenant.toDto(), this.roles, this.userInfo.toDto())
}

