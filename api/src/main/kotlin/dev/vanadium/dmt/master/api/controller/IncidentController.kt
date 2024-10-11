package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.IncidentDto
import dev.vanadium.dmt.master.api.dto.enrichers.UserDtoEnricher
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.commons.error.IncidentService
import dev.vanadium.dmt.master.commons.rbac.RequireRole
import dev.vanadium.dmt.master.commons.rbac.UserRole
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/incident")
@Tag(name = DmtSpecificationTags.INCIDENT)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class IncidentController {

    @Autowired
    private lateinit var userDtoEnricher: UserDtoEnricher

    @Autowired
    private lateinit var incidentService: IncidentService

    @GetMapping(produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns a list of incidents, filtered by whether they are resolved or not and ordered by the creation date descending")
    @RequireRole([UserRole.INCIDENT_MANAGER])
    fun getIncidents(
        @QueryParam("page") @Min(0) page: Int,
        @QueryParam("pageSize") @Min(0) @Max(100) pageSize: Int,
        @QueryParam("resolved") resolved: Boolean
    ): Page<IncidentDto> {
        return incidentService.getIncidents(
            resolved,
            PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).map { it.toDto(userDtoEnricher) }
    }

    @PutMapping("/{incidentId}/resolve", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Resolves an incident")
    @RequireRole([UserRole.INCIDENT_MANAGER])
    fun resolveIncident(@PathVariable incidentId: UUID) {
        incidentService.resolveIncident(incidentId)
    }

}