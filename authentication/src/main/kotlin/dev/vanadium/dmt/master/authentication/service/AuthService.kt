package dev.vanadium.dmt.master.authentication.service

import dev.vanadium.dmt.master.authentication.dto.LoginTokenDto
import dev.vanadium.dmt.master.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AuthService {

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
}