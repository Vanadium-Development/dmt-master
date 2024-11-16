package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationSecurityRequirements
import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import dev.vanadium.dmt.master.api.dto.FileDto
import dev.vanadium.dmt.master.api.dto.PreflightFileDto
import dev.vanadium.dmt.master.api.dto.UpdateFileDto
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.service.file.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.ws.rs.core.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.util.UUID

@RestController
@RequestMapping("/file")
@Tag(name = DmtSpecificationTags.FILE)
@SecurityRequirement(name = DmtSpecificationSecurityRequirements.TOKEN)
class FileController {

    @Autowired
    private lateinit var fileService: FileService



    @GetMapping("/{dfid}", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns information about a single file")
    fun getFile(@PathVariable dfid: UUID): FileDto {
        return fileService.getAuthorizedByDfid(dfid).toDto()
    }

    @PutMapping("/{dfid}", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Updates data of a file")
    fun updateFile(@PathVariable dfid: UUID, @RequestBody body: UpdateFileDto) {
        fileService.updateFile(dfid, body.fileName)
    }


    @DeleteMapping("/{dfid}")
    @Operation(summary = "Deletes a file")
    fun deleteFile(@PathVariable dfid: UUID) {
        fileService.deleteFile(dfid)
    }

    @GetMapping("/{dfid}/download", produces = [MediaType.APPLICATION_OCTET_STREAM])
    @Operation(summary = "Servers the file as a download")
    fun downloadFile(@PathVariable dfid: UUID): ResponseEntity<StreamingResponseBody> {
        return fileService.downloadFile(dfid)
    }

    @PostMapping("/upload/{preflightToken}", consumes = [MediaType.MULTIPART_FORM_DATA], produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Uploads a file")
    fun uploadFile(@RequestParam("file") file: MultipartFile, @PathVariable preflightToken: String): FileDto {
        file.inputStream.use {
            val uploadedFile = fileService.finalizeFileUpload(preflightToken, file, file.size)

            return uploadedFile.toDto()
        }
    }

    @PostMapping("/upload/preflight", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Preflights a file upload with necessary metadata")
    fun preflightFile(@RequestBody dto: PreflightFileDto.Request): PreflightFileDto.Response {

        val token = fileService.preflightFile(dto.name)

        return PreflightFileDto.Response(token.first, token.second)
    }
}