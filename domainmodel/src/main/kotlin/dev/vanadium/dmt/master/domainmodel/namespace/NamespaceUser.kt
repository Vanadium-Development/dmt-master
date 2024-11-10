package dev.vanadium.dmt.master.domainmodel.namespace

import dev.vanadium.dmt.master.domainmodel.user.User
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "namespace_user")
class NamespaceUser(
    @EmbeddedId
    val id: NamespaceUserId,
    @JoinColumn(name = "created_by")
    @ManyToOne
    var createdBy: User?
) {
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
}


@Embeddable
data class NamespaceUserId(
    @JoinColumn(name = "user_id")
    @ManyToOne
    var user: User,
    @JoinColumn(name = "namespace_id")
    @ManyToOne
    var namespace: Namespace
)