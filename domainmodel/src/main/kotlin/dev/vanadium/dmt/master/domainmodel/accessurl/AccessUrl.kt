package dev.vanadium.dmt.master.domainmodel.accessurl

import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import dev.vanadium.dmt.master.domainmodel.user.User
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "access_url")
class AccessUrl(
    @Column(name = "slug")
    val slug: String,
    @JoinColumn(name = "namespace_id")
    @ManyToOne
    val namespace: Namespace,
    @JoinColumn(name = "created_by")
    @ManyToOne
    val createdBy: User,
    @Column(name = "settings")
    @Type(JsonBinaryType::class)
    val settings: AccessUrlSettings,
    @JoinColumn(name = "dfid")
    @ManyToOne
    val file: DistributedFile
) {

    @Id
    @Column(name = "access_url_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()

    @Column(name = "enabled")
    var enabled: Boolean = true
}