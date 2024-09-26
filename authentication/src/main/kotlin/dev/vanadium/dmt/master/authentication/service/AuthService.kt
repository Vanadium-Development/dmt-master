package dev.vanadium.dmt.master.authentication.service

import com.auth0.jwt.exceptions.JWTVerificationException
import dev.vanadium.dmt.master.authentication.dto.LoginTokenDto
import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class AuthService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    fun login(externalId: String): LoginTokenDto {

        val user = userService.bootstrapUser(externalId)

        val accessToken = tokenService.createAccessToken(user.id)
        val refreshToken = tokenService.createRefreshToken(accessToken.first, user.id)

        return LoginTokenDto(accessToken.second, refreshToken)
    }

    fun resolveUserContext(token: String): UserContext? {
        try {
            val verifiedToken = tokenService.tokenVerification()
                .verify(token)

            val userId = UUID.fromString(verifiedToken.subject ?: return null)

            val user = userService.getById(userId) ?: return null

            return UserContext(user, findRoles())
        } catch (e: JWTVerificationException) {
            logger.error("Token verification failed with following error:", e)
            return null
        }
    }

    private fun findRoles(): List<String> {
        return listOf("Test", "roles")
    }
}