package dev.vanadium.dmt.master.authentication

import dev.vanadium.dmt.master.commons.authentication.UserContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class DMTAuthentication(
    val userContext: UserContext
) : Authentication {
    override fun getName(): String {
        return userContext.tenant.id.toString()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableSetOf()
    }

    override fun getCredentials(): Any {
        return "n/a"
    }

    override fun getDetails(): Any {
        return "n/a"
    }

    override fun getPrincipal(): UserContext {
        return userContext
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        // noop
    }
}