package dev.vanadium.dmt.master.domainmodel.namespace

import dev.vanadium.dmt.master.domainmodel.user.User
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "namespace", schema = "public")
class Namespace(
    @Column(name = "display_name")
    var displayName: String,
    @Column(name = "url_slug")
    var urlSlug: String,
    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: User,
) {


    @Id
    @Column(name = "namespace_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()

    @Column(name = "enabled")
    var enabled = true


    override fun toString(): String {
        return "$displayName ($id)"
    }
}