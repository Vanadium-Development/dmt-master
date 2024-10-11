package dev.vanadium.dmt.master.commons.error

import org.springframework.http.HttpStatus

enum class DmtError(val code: Int, val template: String, val status: HttpStatus, val shouldLog: Boolean = false) {

    USER_NOT_FOUND(1000, "Provided user with id '%s' was not found.", HttpStatus.NOT_FOUND, false),
    FILE_UPLOAD_ERROR(1001, "Failed to upload file: %s", HttpStatus.UNPROCESSABLE_ENTITY, false),
    INSUFFICIENT_PERMISSIONS(1002, "Insufficient permissions", HttpStatus.FORBIDDEN, false),
    INCIDENT_NOT_FOUND(1003, "Incident with id '%s' was not found.", HttpStatus.NOT_FOUND, false),

    DTO_ENRICHMENT_ERROR(9001, "An error occurred while enriching DTO '%s' -> '%s': %s", HttpStatus.INTERNAL_SERVER_ERROR, true),
    INTERNAL_VALIDATION_ERROR(9002, "An internal validation error occurred: %s", HttpStatus.INTERNAL_SERVER_ERROR, true),
    UNKNOWN_ERROR(9999, "An unknown error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, true);


    fun toThrowableException(cause: Throwable?, vararg args: Any): DmtApiException {
        return DmtApiException(String.format(template, *args), this, cause)
    }
}
