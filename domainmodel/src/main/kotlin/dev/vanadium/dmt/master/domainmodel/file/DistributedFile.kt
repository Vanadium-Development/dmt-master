package dev.vanadium.dmt.master.domainmodel.file

import dev.vanadium.dmt.master.domainmodel.user.User
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "distributed_file", schema = "public")
class DistributedFile(
    @Column(name = "name")
    var name: String,
    @Column(name = "object_id")
    var objectId: String?,
    @JoinColumn(name = "created_by")
    @ManyToOne
    var createdBy: User
) {

    @Id
    @Column(name = "dfid")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var dfid: UUID

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status = DistributedFileStatus.PREFLIGHT

    override fun toString(): String {
        return "$name (dfid=$dfid; objectId=$objectId; status=$status)"
    }
}