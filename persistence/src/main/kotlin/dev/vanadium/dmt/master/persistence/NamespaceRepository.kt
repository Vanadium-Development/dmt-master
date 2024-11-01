package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface NamespaceRepository : JpaRepository<Namespace, UUID> {


    @Query("select count(*) >= 1 from Namespace n where n.urlSlug = :slug")
    fun slugExists(slug: String): Boolean

    @Query("select n from Namespace n where n.createdBy.id = :user")
    fun findByCreatedBy(user: UUID, pageable: Pageable): Page<Namespace>
}