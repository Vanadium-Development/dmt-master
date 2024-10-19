package dev.vanadium.dmt.master.commons.error

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

data class ErrorResponse(
    val error: Error,
    val request: Request
) {

    @Schema(name = "ErrorResponseError")
    data class Error(
        val code: Int,
        val status: String,
        val statusCode: Int,
        val message: String
    )

    @Schema(name = "ErrorResponseRequest")
    data class Request(
        val timestamp: Instant,
        val path: String,
        val correlationId: String?,
    )
}