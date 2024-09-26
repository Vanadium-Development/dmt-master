package dev.vanadium.dmt.master.authentication.config

import dev.vanadium.dmt.master.authentication.OAuthSuccessHandler
import dev.vanadium.dmt.master.authentication.filter.TokenFilter
import dev.vanadium.dmt.master.authentication.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Autowired
    private lateinit var oAuthSuccessHandler: OAuthSuccessHandler

    @Autowired
    private lateinit var authService: AuthService

    @Bean
    @Order(3)
    fun tokenConfig(http: HttpSecurity): SecurityFilterChain {

        http {
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            securityMatcher("/**")

            authorizeRequests {
                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                TokenFilter(authService)
            )
        }

        return http.build()
    }

    @Bean
    @Order(1)
    fun publicConfig(http: HttpSecurity): SecurityFilterChain {
        http {
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            securityMatcher("/")

            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
        }

        return http.build()
    }

    @Bean
    @Order(2)
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