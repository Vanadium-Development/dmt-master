package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.FileDto
import dev.vanadium.dmt.master.api.dto.PreflightFileDto
import dev.vanadium.dmt.master.api.dto.enrichers.UserDtoEnricher
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.service.file.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.ws.rs.core.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/file")
@Tag(name = DmtSpecificationTags.FILE)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class FileController {

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var userDtoEnricher: UserDtoEnricher


    @PostMapping("/upload/{preflightToken}", consumes = [MediaType.MULTIPART_FORM_DATA])
    @Operation(summary = "Uploads a file")
    fun uploadFile(@RequestParam("file") file: MultipartFile, @PathVariable preflightToken: String): FileDto {
        val uploadedFile = fileService.finalizeFileUpload(preflightToken, file.bytes)

        val enrichedUser = userDtoEnricher.enrich(uploadedFile.createdBy.toDto())

        return uploadedFile.toDto(enrichedUser)
    }

    @PostMapping("/upload/preflight", consumes = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Preflights a file upload with necessary metadata")
    fun preflightFile(@RequestBody dto: PreflightFileDto.Request): PreflightFileDto.Response {

        val token = fileService.preflightFile(dto.name)

        return PreflightFileDto.Response(token.first, token.second)
    }
}