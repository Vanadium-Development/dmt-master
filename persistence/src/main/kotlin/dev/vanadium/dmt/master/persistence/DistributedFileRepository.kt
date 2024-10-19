package dev.vanadium.dmt.master.persistence

import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface DistributedFileRepository : JpaRepository<DistributedFile, UUID> {


    fun existsByObjectId(objectId: String): Boolean


    @Query("""select d from DistributedFile d where d.status = 'PREFLIGHT'""")
    fun findPreflightFiles(): List<DistributedFile>

    @Query("""select d from DistributedFile d where d.createdBy.id = :user and d.status = 'UPLOADED' """)
    fun findByUserUploaded(pageable: Pageable, user: UUID): Page<DistributedFile>
}