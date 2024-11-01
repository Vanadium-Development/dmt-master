package dev.vanadium.dmt.master.domainmodel.user

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

    @Column(name = "username")
    var username: String? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    override fun toString(): String {
        return "$id -> $externalId"
    }
}