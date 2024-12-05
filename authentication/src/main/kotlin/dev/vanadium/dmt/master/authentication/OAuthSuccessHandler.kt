package dev.vanadium.dmt.master.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanadium.dmt.master.authentication.properties.AuthenticationProperties
import dev.vanadium.dmt.master.authentication.service.AuthService
import dev.vanadium.dmt.master.commons.utils.sha256
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class OAuthSuccessHandler : AuthenticationSuccessHandler {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var authenticationProperties: AuthenticationProperties

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val principal = authentication.principal as OAuth2User

        val token = objectMapper.writeValueAsBytes(authService.login(principal.name))

        response.sendRedirect(authenticationProperties.postAuthenticationRedirect.replace("{token}", Base64.getUrlEncoder().withoutPadding().encodeToString(token)))
    }
}