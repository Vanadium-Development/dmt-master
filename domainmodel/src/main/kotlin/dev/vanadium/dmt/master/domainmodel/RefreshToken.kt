package dev.vanadium.dmt.master.domainmodel

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Column(name = "token")
    val token: String,
    @JoinColumn(name = "user_id")
    @ManyToOne
    val user: User,
    @Column(name = "expires_at")
    val expiresAt: Instant,
    @Column(name = "access_token_id")
    var accessToken: UUID
) {

    @Id
    @Column(name = "refresh_token_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID
}