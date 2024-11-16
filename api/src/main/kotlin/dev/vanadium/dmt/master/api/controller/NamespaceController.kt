package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.*
import dev.vanadium.dmt.master.commons.context.NamespaceContext
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
    private lateinit var namespaceContext: NamespaceContext

    @Autowired
    private lateinit var namespaceService: NamespaceService



    @PostMapping
    @Operation(summary = "Creates a namespace")
    fun createNamespace(@RequestBody body: CreateNamespaceDto): NamespaceDto {
        val namespace = namespaceService.createNamespace(body.name)

        return namespace.toDto()
    }

    @GetMapping("/{namespaceId}")
    @Operation(summary = "Retrieve a namespace by id")
    fun getNamespace(@PathVariable namespaceId: UUID): NamespaceDto {
        return namespaceService.getAuthorizedNamespace(namespaceId).toDto()
    }

    @GetMapping("/{namespaceId}/name")
    @Operation(summary = "Returns the name of the namespace")
    fun getNamespaceName(@PathVariable namespaceId: UUID): String {
        return namespaceContext().createdBy.username ?: "ABCD"
    }

    @GetMapping("/{namespaceId}/member")
    @Operation(summary = "Returns all members of given namespace")
    fun getMembers(@PathVariable namespaceId: UUID, @Min(0) @QueryParam("page") page: Int, @Min(0) @Max(100) @QueryParam("pageSize") pageSize: Int): Page<NamespaceUserDto> {
        return namespaceService.getMembers(PageRequest.of(page, pageSize)).map { it.toDto() }
    }

    @PostMapping("/{namespaceId}/member")
    @Operation(summary = "Adds a member to a namespace")
    fun addMember(@PathVariable namespaceId: UUID, @RequestBody body: NamespaceUserAddDto) {
        namespaceService.addMember(body.user)
    }

    @DeleteMapping("/{namespaceId}/member/{member}")
    @Operation(summary = "Removes a member from a namespace")
    fun removeMember(@PathVariable namespaceId: UUID, @PathVariable member: UUID) {
        namespaceService.removeMember(member)
    }

    @PostMapping("/{namespaceId}/member/suggestion")
    @Operation(summary = "Queries users, which can be added to the namespace")
    fun queryMemberSuggestions(@PathVariable namespaceId: UUID, @QueryParam("q") query: String): List<UserDto> {
        return namespaceService.queryMemberSuggestion(query).map { it.toDto() }
    }

}