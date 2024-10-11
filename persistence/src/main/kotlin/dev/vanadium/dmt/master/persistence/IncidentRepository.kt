package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.incident.Incident
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IncidentRepository : JpaRepository<Incident, UUID> {


    @Query("select i from Incident i where (:resolved = true and i.resolvedAt is not null) or (:resolved = false and i.resolvedAt is null)")
    fun findByResolved(resolved: Boolean, pageable: Pageable): Page<Incident>
}