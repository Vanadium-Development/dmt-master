package dev.vanadium.dmt.master.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanadium.dmt.master.commons.correlation.MDCUtils
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.commons.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TokenEntryPoint : AuthenticationEntryPoint {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {


        response.status = 401
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.writer.write(objectMapper.writeValueAsString(ErrorResponse(
            ErrorResponse.Error(DmtError.UNAUTHORIZED.code, HttpStatus.UNAUTHORIZED.name, HttpStatus.UNAUTHORIZED.value(), DmtError.UNAUTHORIZED.template),
            ErrorResponse.Request(Instant.now(), request.servletPath, MDCUtils.getCorrelationId())
        )))
    }
}