package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.CreateNamespaceDto
import dev.vanadium.dmt.master.api.dto.NamespaceDto
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.service.namespace.NamespaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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
}