package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByExternalId(externalId: String): User?
}