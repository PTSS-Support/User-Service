package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import java.time.OffsetDateTime
import java.util.UUID
import jakarta.persistence.*

@Entity
@Table(
    name = "user_invitations",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
class InvitationEntity : BaseEntity() {
    @Id
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "email", nullable = false)
    lateinit var email: String

    @Column(name = "role", nullable = false)
    lateinit var role: String

    @Column(name = "verification_code", nullable = false)
    lateinit var verificationCode: String

    @Column(name = "group_id")
    var groupId: UUID? = null

    @Column(name = "expires_at", nullable = false)
    lateinit var expiresAt: OffsetDateTime

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false

    @Column(name = "is_registered", nullable = false)
    var isRegistered: Boolean = false

    companion object : PanacheCompanion<InvitationEntity> {
        fun findByEmailAndVerificationCode(email: String, code: String): InvitationEntity? =
            find("email = ?1 and verificationCode = ?2 and isVerified = false and expiresAt > CURRENT_TIMESTAMP",
                email, code).firstResult()
    }
}