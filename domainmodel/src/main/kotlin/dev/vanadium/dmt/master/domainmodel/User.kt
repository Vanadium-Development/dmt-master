package dev.vanadium.dmt.master.domainmodel

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user", schema = "public")
class User(
    @Column(name = "external_id")
    var externalId: String
) {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    override fun toString(): String {
        return "$id -> $externalId"
    }
}