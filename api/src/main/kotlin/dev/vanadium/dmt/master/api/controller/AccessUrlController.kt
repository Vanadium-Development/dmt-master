package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.AccessUrlCreateDto
import dev.vanadium.dmt.master.api.dto.AccessUrlDto
import dev.vanadium.dmt.master.api.dto.NamespaceDto
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.commons.context.NamespaceContext
import dev.vanadium.dmt.master.service.accessurl.AccessUrlService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.ws.rs.QueryParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = DmtSpecificationTags.ACCESS_URL)
@RequestMapping("/namespace/{namespaceId}/access-url")
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class AccessUrlController(
    private val accessUrlService: AccessUrlService,
    private val namespaceContext: NamespaceContext
) {

    @PostMapping
    @Operation(summary = "Creates an access url for a namespace")
    fun createAccessUrl(@PathVariable namespaceId: UUID, @RequestBody body: AccessUrlCreateDto): AccessUrlDto {
        return accessUrlService.createAccessUrl(body.slug, body.settings, body.file).toDto()
    }

    @GetMapping
    @Operation(summary = "Retrieves a paginated list of access urls by namespace")
    fun getAccessUrls(@PathVariable namespaceId: UUID, @QueryParam("page") page: Int, @QueryParam("pageSize") pageSize: Int): Page<AccessUrlDto> {
        return accessUrlService.getAccessUrls(PageRequest.of(page, pageSize)).map { it.toDto() }
    }

    @GetMapping("oOOOO")
    fun getNamespace(@PathVariable namespaceId: UUID): NamespaceDto {
        return namespaceContext().toDto()
    }
}