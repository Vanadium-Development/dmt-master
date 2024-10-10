package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.user.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
}