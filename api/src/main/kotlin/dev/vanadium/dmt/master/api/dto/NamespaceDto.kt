package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import dev.vanadium.dmt.master.domainmodel.namespace.NamespaceUser
import java.time.Instant
import java.util.UUID

data class NamespaceDto(
    val id: UUID,
    val urlSlug: String,
    val displayName: String,
    val createdBy: UserDto,
    val createdAt: Instant,
    val enabled: Boolean
)

data class CreateNamespaceDto(
    val name: String
)

fun Namespace.toDto(): NamespaceDto {
    return NamespaceDto(
        this.id,  this.urlSlug, this.displayName, this.createdBy.toDto(), this.createdAt, this.enabled
    )
}



data class NamespaceUserDto(
    val user: UserDto,
    val createdBy: UserDto?,
    val createdAt: Instant
)

fun NamespaceUser.toDto(): NamespaceUserDto {
    return NamespaceUserDto(this.id.user.toDto(), this.createdBy?.toDto(), this.createdAt)
}

data class NamespaceUserAddDto(
    val user: UUID
)