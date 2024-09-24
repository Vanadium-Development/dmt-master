package dev.vanadium.dmt.master.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuthSuccessHandler : AuthenticationSuccessHandler {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {



        response.writer.write(objectMapper.writeValueAsString(authentication.principal))
    }
}