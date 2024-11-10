package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByExternalId(externalId: String): User?

    @Query("select u from User u where lower(u.username) like %:username%")
    fun queryByUsername(username: String, pageable: Pageable): Page<User>
}