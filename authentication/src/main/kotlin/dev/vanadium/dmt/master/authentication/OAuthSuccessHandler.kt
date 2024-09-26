package dev.vanadium.dmt.master.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanadium.dmt.master.authentication.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuthSuccessHandler : AuthenticationSuccessHandler {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authService: AuthService

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val principal = authentication.principal as OAuth2User


        response.writer.write(objectMapper.writeValueAsString(authService.login(principal.name)))
    }
}