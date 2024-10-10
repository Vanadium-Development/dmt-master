package dev.vanadium.dmt.master.authentication.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.vanadium.dmt.master.authentication.dto.TokenHolder
import dev.vanadium.dmt.master.authentication.properties.AuthenticationProperties
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.domainmodel.user.RefreshToken
import dev.vanadium.dmt.master.persistence.RefreshTokenRepository
import dev.vanadium.dmt.master.service.user.UserService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.random.Random

@Service
class TokenService {

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var authenticationProperties: AuthenticationProperties

    private lateinit var algorithm: Algorithm

    @PostConstruct
    fun init() {
        this.algorithm = Algorithm.HMAC256(authenticationProperties.jwtSecret)
    }


    fun createAccessToken(userId: UUID): Pair<UUID, TokenHolder> {
        val tokenId = UUID.randomUUID()

        val expiresAt = Instant.now() + authenticationProperties.accessTokenExpiration
        return tokenId to TokenHolder(
            JWT.create()
                .withAudience(authenticationProperties.jwtAudiencePrefix + "access-token")
                .withIssuer(authenticationProperties.jwtIssuer)
                .withClaim("jti", tokenId.toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(expiresAt)
                .withSubject(userId.toString())
                .sign(algorithm), expiresAt
        )
    }

    fun tokenVerification(): JWTVerifier {
        return JWT.require(algorithm)
            .withAudience(authenticationProperties.jwtAudiencePrefix + "access-token")
            .withIssuer(authenticationProperties.jwtIssuer)
            .withClaimPresence("jti")
            .withClaimPresence("sub")
            .build()
    }

    fun createRefreshToken(accessTokenId: UUID, userId: UUID): TokenHolder {
        val user = userService.getById(userId) ?: throw DmtError.USER_NOT_FOUND.toThrowableException(null, userId)
        val token = generateToken()
        val expiresAt = Instant.now() + authenticationProperties.refreshTokenExpiration
        refreshTokenRepository.save(RefreshToken(token, user, expiresAt, accessTokenId))

        return TokenHolder(token, expiresAt)
    }

    private fun generateToken(): String {
        val bytes = Random.nextBytes(64)
        return String(Base64.getEncoder().encode(bytes))
    }
}