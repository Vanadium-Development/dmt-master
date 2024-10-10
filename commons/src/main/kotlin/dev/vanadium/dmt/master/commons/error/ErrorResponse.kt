package dev.vanadium.dmt.master.commons.error

import java.time.Instant

data class ErrorResponse(
    val error: Error,
    val request: Request
) {
    data class Error(
        val code: Int,
        val status: String,
        val statusCode: Int,
        val message: String
    )
    data class Request(
        val timestamp: Instant,
        val path: String,
        val correlationId: String,
    )
}