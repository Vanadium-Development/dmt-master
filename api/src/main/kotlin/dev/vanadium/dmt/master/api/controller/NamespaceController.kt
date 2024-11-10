package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.*
import dev.vanadium.dmt.master.service.namespace.NamespaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.ws.rs.QueryParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/namespace")
@Tag(name = DmtSpecificationTags.NAMESPACE)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class NamespaceController {

    @Autowired
    private lateinit var namespaceService: NamespaceService



    @PostMapping
    @Operation(summary = "Creates a namespace")
    fun createNamespace(@RequestBody body: CreateNamespaceDto): NamespaceDto {
        val namespace = namespaceService.createNamespace(body.name)

        return namespace.toDto()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a namespace by id")
    fun getNamespace(@PathVariable id: UUID): NamespaceDto {
        return namespaceService.getAuthorizedNamespace(id).toDto()
    }

    @GetMapping("/{id}/member")
    @Operation(summary = "Returns all members of given namespace")
    fun getMembers(@PathVariable id: UUID, @Min(0) @QueryParam("page") page: Int, @Min(0) @Max(100) @QueryParam("pageSize") pageSize: Int): Page<NamespaceUserDto> {
        return namespaceService.getMembers(id, PageRequest.of(page, pageSize)).map { it.toDto() }
    }

    @PostMapping("/{id}/member")
    @Operation(summary = "Adds a member to a namespace")
    fun addMember(@PathVariable id: UUID, @RequestBody body: NamespaceUserAddDto) {
        namespaceService.addMember(id, body.user)
    }

    @DeleteMapping("/{id}/member/{member}")
    @Operation(summary = "Removes a member from a namespace")
    fun removeMember(@PathVariable id: UUID, @PathVariable member: UUID) {
        namespaceService.removeMember(id, member)
    }

    @PostMapping("/{id}/member/suggestion")
    @Operation(summary = "Queries users, which can be added to the namespace")
    fun queryMemberSuggestions(@PathVariable id: UUID, @QueryParam("q") query: String): List<UserDto> {
        return namespaceService.queryMemberSuggestion(id, query).map { it.toDto() }
    }

}