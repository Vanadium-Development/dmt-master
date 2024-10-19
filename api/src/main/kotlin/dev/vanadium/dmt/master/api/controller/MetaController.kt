package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.commons.error.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
@RequestMapping("/meta")
@Tag(name = DmtSpecificationTags.META)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class MetaController {

    @GetMapping("/error-response")
    @Operation(summary = "This endpoint returns an example error response, so that the model is available in the OpenAPI schema")
    fun getErrorResponse(): ErrorResponse {
        return ErrorResponse(
            ErrorResponse.Error(1337, "Test", 200, "This is an example"),
            ErrorResponse.Request(Instant.now(), "/ddd", "")
        )
    }
}