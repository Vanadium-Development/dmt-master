package dev.vanadium.dmt.master.authentication.dto

import java.time.Instant

data class TokenHolder(
    val token: String,
    val expiresAt: Instant
)

data class LoginTokenDto(
    val accessToken: TokenHolder,
    val refreshToken: TokenHolder
)