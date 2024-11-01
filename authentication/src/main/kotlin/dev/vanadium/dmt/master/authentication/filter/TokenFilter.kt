package dev.vanadium.dmt.master.authentication.filter

import dev.vanadium.dmt.master.authentication.DMTAuthentication
import dev.vanadium.dmt.master.authentication.service.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.filter.OncePerRequestFilter

class TokenFilter(
    private val authService: AuthService
) : OncePerRequestFilter() {


    private val repository = RequestAttributeSecurityContextRepository()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = request.getHeader("Authorization")
            ?.split(" ")
            ?.getOrNull(1) ?: run {

            sendUnauthorized(response)
            return
        }

        val userContext = authService.resolveUserContext(token) ?: run {
            sendUnauthorized(response)
            return
        }

        val authentication = DMTAuthentication(userContext)

        val ctx = SecurityContextHolder.createEmptyContext()
        ctx.authentication = authentication

        SecurityContextHolder.setContext(ctx)
        repository.saveContext(ctx, request, response)


        filterChain.doFilter(request, response)
    }

    private fun sendUnauthorized(response: HttpServletResponse) {
        response.writer.write("Unauthorized")
        response.status = 401
        response.contentType = "text/plain"
    }
}