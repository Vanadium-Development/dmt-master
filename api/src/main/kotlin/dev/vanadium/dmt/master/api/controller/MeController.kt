package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.AnonymousFileDto
import dev.vanadium.dmt.master.api.dto.UserContextDto
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.commons.authentication.UserContext
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

    @GetMapping(produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns information about currently logged-in user.")
    fun getMe(): UserContextDto {
        return userContext.toDto()
    }

    @GetMapping("/file", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns all files created and owned by the user")
    fun getFiles(@Min(0) @QueryParam("page") page: Int, @Min(0) @Max(100) @QueryParam("pageSize") pageSize: Int): Page<AnonymousFileDto> {
        return meService.getOwnedFiles(PageRequest.of(page, pageSize)).map { it.toDto() }
    }
}