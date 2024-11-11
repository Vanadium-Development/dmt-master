package dev.vanadium.dmt.master.commons.error

import org.springframework.http.HttpStatus

enum class DmtError(val code: Int, val template: String, val status: HttpStatus, val shouldLog: Boolean = false) {

    USER_NOT_FOUND(1000, "Provided user with id '%s' was not found.", HttpStatus.NOT_FOUND, false),
    FILE_UPLOAD_ERROR(1001, "Failed to upload file: %s", HttpStatus.UNPROCESSABLE_ENTITY, false),
    INSUFFICIENT_PERMISSIONS(1002, "Insufficient permissions", HttpStatus.FORBIDDEN, false),
    INCIDENT_NOT_FOUND(1003, "Incident with id '%s' was not found.", HttpStatus.NOT_FOUND, false),
    FILE_PREFLIGHT_ERROR(1004, "Failed to preflight file: %s", HttpStatus.UNPROCESSABLE_ENTITY, false),
    FILE_SERVE_ERROR(1005, "Failed to serve file: %s", HttpStatus.UNPROCESSABLE_ENTITY, false),
    NAMESPACE_NOT_FOUND(1006, "Namespace with id '%s' was not found.", HttpStatus.NOT_FOUND, false),
    NAMESPACE_MEMBER_REMOVAL_NOT_POSSIBLE(1007, "Cannot remove member: %s", HttpStatus.FORBIDDEN, false),

    DTO_ENRICHMENT_ERROR(9001, "An error occurred while enriching DTO '%s' -> '%s': %s", HttpStatus.INTERNAL_SERVER_ERROR, true),
    INTERNAL_VALIDATION_ERROR(9002, "An internal validation error occurred: %s", HttpStatus.INTERNAL_SERVER_ERROR, true),

    UNAUTHORIZED(9998, "Unauthorized", HttpStatus.UNAUTHORIZED, false),
    UNKNOWN_ERROR(9999, "An unknown error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, true);


    fun toThrowableException(cause: Throwable?, vararg args: Any): DmtApiException {
        return DmtApiException(String.format(template, *args), this, cause)
    }
}
