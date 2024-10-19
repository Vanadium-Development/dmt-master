package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.api.dto.enrichers.UserDtoEnricher
import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import dev.vanadium.dmt.master.domainmodel.file.DistributedFileStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.*


object PreflightFileDto {
    @Schema(name = "PreflightFileDtoRequest")
    data class Request(
        val name: String
    )
    @Schema(name = "PreflightFileDtoResponse")
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
    val createdBy: EnrichedUserDto,
    val createdAt: Instant,
    val fileSize: Long
)

data class AnonymousFileDto(
    val dfid: UUID,
    val name: String,
    val objectId: String?,
    val status: DistributedFileStatus,
    val createdAt: Instant,
    val fileSize: Long

)

data class UpdateFileDto(
    val fileName: String?
)

fun DistributedFile.toDto(): AnonymousFileDto {
    return AnonymousFileDto(this.dfid, this.name, this.objectId, this.status, this.createdAt, this.fileSize)
}

fun DistributedFile.toDto(enricher: UserDtoEnricher): FileDto {
    return FileDto(this.dfid, this.name, this.objectId, this.status, enricher.enrich(createdBy.toDto()), this.createdAt, this.fileSize)
}
