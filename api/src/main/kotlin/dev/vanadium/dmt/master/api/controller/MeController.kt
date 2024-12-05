package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.*
import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.service.namespace.NamespaceService
import dev.vanadium.dmt.master.service.user.MeService
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/me")
@Tag(name = DmtSpecificationTags.ME)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class MeController {

    @Autowired
    private lateinit var userContext: UserContext


    @Autowired
    private lateinit var meService: MeService

    @Autowired
    private lateinit var namespaceService: NamespaceService


    @GetMapping(produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns information about currently logged-in user.")
    fun getMe(): UserContextDto {
        return userContext.toDto()
    }

    @GetMapping("/file", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns all files created and owned by the user")
    fun getFiles(@Min(0) @QueryParam("page") page: Int, @Min(0) @Max(100) @QueryParam("pageSize") pageSize: Int, @QueryParam("q") query: String?): Page<FileDto> {
        return meService.getOwnedFiles(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")), query).map { it.toDto() }
    }

    @GetMapping("/namespace", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns a list of namespaces, the user has access to")
    fun getNamespaces(@Min(0) @QueryParam("page") page: Int, @Min(0) @Max(100) @QueryParam("pageSize") pageSize: Int): Page<NamespaceDto> {
        return namespaceService.getNamespacesByUser(userContext.tenant.id, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id.namespace.displayName"))).map { it.toDto() }
    }
}