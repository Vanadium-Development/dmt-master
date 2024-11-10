package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.namespace.NamespaceUser
import dev.vanadium.dmt.master.domainmodel.namespace.NamespaceUserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface NamespaceUserRepository : JpaRepository<NamespaceUser, NamespaceUserId> {

    @Query("select count(*) > 0 from NamespaceUser nu where nu.id.user.id = :user and nu.id.namespace.id = :namespace")
    fun existsByUserAndNamespace(user: UUID, namespace: UUID): Boolean

    @Query("select nu from NamespaceUser nu where nu.id.namespace.id = :namespace")
    fun getMembers(namespace: UUID, pageable: Pageable): Page<NamespaceUser>

    @Modifying
    @Query("delete from NamespaceUser nu where nu.id.user.id = :user and nu.id.namespace.id = :namespace")
    fun deleteByUserAndNamespace(user: UUID, namespace: UUID)

}