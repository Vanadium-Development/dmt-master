package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrl
import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrlSettings
import java.time.Instant
import java.util.UUID

data class AccessUrlDto(
    val id: UUID,
    val slug: String,
    val namespace: NamespaceDto,
    val createdBy: UserDto,
    val settings: AccessUrlSettings,
    val file: FileDto,
    val createdAt: Instant,
    val enabled: Boolean
)

fun AccessUrl.toDto(): AccessUrlDto {
    return AccessUrlDto(
        this.id,
        this.slug,
        this.namespace.toDto(),
        this.createdBy.toDto(),
        this.settings,
        this.file.toDto(),
        this.createdAt,
        this.enabled
    )
}

data class AccessUrlCreateDto(
    val slug: String,
    val settings: AccessUrlSettings,
    val file: UUID
)

data class AccessUrlSlugAvailableDto(
    val slug: String,
    val available: Boolean,
    val reason: String?
)