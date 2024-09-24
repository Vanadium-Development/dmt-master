package dev.vanadium.dmt.master.authentication.config

import dev.vanadium.dmt.master.authentication.OAuthSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Autowired
    private lateinit var oAuthSuccessHandler: OAuthSuccessHandler

    @Bean
    fun oauth2Config(http: HttpSecurity): SecurityFilterChain {

        http {
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }


            securityMatcher("/auth/login", "/oauth2/**", "/login/oauth2/**")
            authorizeRequests {
                authorize(anyRequest, authenticated)

            }

            oauth2Login {
                authenticationSuccessHandler = oAuthSuccessHandler
            }
        }

        return http.build()
    }
}