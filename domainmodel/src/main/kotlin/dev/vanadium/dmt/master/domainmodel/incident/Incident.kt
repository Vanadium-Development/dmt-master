package dev.vanadium.dmt.master.domainmodel.incident

import dev.vanadium.dmt.master.domainmodel.user.User
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "incident", schema = "public")
class Incident(
    @Column(name = "correlation_id")
    var correlationId: String?,
    @Column(name = "stack_trace")
    var stackTrace: String,
    @Column(name = "created_at")
    var createdAt: Instant,
    @Column(name = "resolved_at")
    var resolvedAt: Instant?,
    @JoinColumn(name = "resolved_by")
    @ManyToOne
    var resolvedBy: User?
) {
    @Id
    @Column(name = "incident_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    override fun toString(): String {
        return id.toString()
    }


    fun resolve(user: User) {
        this.resolvedAt = Instant.now()
        this.resolvedBy = user
    }

    fun isResolved() = this.resolvedAt != null
}