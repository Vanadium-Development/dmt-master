package dev.vanadium.dmt.master.authentication

import dev.vanadium.dmt.master.commons.authentication.UserContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.annotation.RequestScope

@Configuration
class UserContextProvider {

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Lazy
    fun authorizedUserContext(): UserContext {
        return SecurityContextHolder.getContext().authentication.principal as UserContext
    }
}