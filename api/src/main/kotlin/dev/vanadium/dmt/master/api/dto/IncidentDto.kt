package dev.vanadium.dmt.master.api.dto

import dev.vanadium.dmt.master.domainmodel.incident.Incident
import java.time.Instant
import java.util.*

data class IncidentDto(
    val id: UUID,
    val correlationId: String?,
    val stackTrace: String,
    val createdAt: Instant,
    val resolvedAt: Instant?,
    val resolvedBy: UserDto?
)


fun Incident.toDto() = IncidentDto(
    this.id,
    this.correlationId,
    this.stackTrace,
    this.createdAt,
    this.resolvedAt,
    this.resolvedBy?.toDto()
)
