package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface AccessUrlRepository : JpaRepository<AccessUrl, UUID> {


    @Query("select count(*) >= 1 from AccessUrl u where u.slug = :slug and u.namespace.id = :namespaceId")
    fun doesSlugExist(namespaceId: UUID, slug: String): Boolean

    @Query("select u from AccessUrl u where u.namespace.id = :namespaceId")
    fun findByNamespace(namespaceId: UUID, pageable: Pageable): Page<AccessUrl>
}