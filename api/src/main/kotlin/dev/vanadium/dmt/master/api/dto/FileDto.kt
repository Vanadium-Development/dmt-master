package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import dev.vanadium.dmt.master.domainmodel.file.DistributedFileStatus
import java.time.Instant
import java.util.*


object PreflightFileDto {
    data class Request(
        val name: String
    )
    data class Response(
        val token: String,
        val expiresAt: Instant
    )
}

data class FileDto(
    val dfid: UUID,
    val name: String,
    val objectId: String?,
    val status: DistributedFileStatus,
    val createdBy: EnrichedUserDto
)

data class AnonymousFileDto(
    val dfid: UUID,
    val name: String,
    val objectId: String?,
    val status: DistributedFileStatus
)


fun DistributedFile.toDto(): AnonymousFileDto {
    return AnonymousFileDto(this.dfid, this.name, this.objectId, this.status)
}

fun DistributedFile.toDto(createdBy: EnrichedUserDto): FileDto {
    return FileDto(this.dfid, this.name, this.objectId, this.status, createdBy)
}
