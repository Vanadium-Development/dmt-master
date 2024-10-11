package dev.vanadium.dmt.master.commons.error

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.correlation.MDCUtils
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@Component
@RestControllerAdvice
class ErrorHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var incidentService: IncidentService

    @Autowired
    private lateinit var userContext: UserContext

    @PostConstruct
    fun errorSanityCheck() {
        DmtError.entries.groupBy { it.code }.filter { it.value.size >= 2 }.map { it.key }.forEach {
            throw RuntimeException("Sanity Check for Errors failed: Error code $it appears more than once.")
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return when(exception) {
            is DmtApiException -> createErrorResponse(exception, request, exception.error.code, exception.error.status, exception.error.shouldLog)
            is org.springframework.web.ErrorResponse -> createErrorResponse(exception, request, 0, HttpStatus.valueOf(exception.statusCode.value()), !exception.statusCode.is4xxClientError)
            else -> createErrorResponse(exception, request, 9999, HttpStatus.INTERNAL_SERVER_ERROR, true)
        }
    }

    private fun createErrorResponse(exception: Exception, request: HttpServletRequest, code: Int, status: HttpStatus, shouldLog: Boolean): ResponseEntity<ErrorResponse> {
        if(shouldLog) {
            logger.error("An unexpected error occurred (current user context=$userContext) ", exception)
            incidentService.storeIncident(exception)
        }

        logger.debug("Error handler caught following exception (shouldLog=$shouldLog, current user context=$userContext): ", exception)

        return ResponseEntity.status(status).body(
            ErrorResponse(
                ErrorResponse.Error(code, status.name, status.value(), if(shouldLog) "An unexpected error occurred." else exception.message ?: ""),
                ErrorResponse.Request(Instant.now(), request.servletPath, MDCUtils.getCorrelationId())
            )
        )
    }
}
