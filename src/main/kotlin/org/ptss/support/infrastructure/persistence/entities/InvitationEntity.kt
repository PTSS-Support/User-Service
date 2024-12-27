package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import java.time.OffsetDateTime
import java.util.UUID
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import org.ptss.support.domain.enums.Role

@Entity
@Table(
    name = "user_invitations",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
class InvitationEntity : BaseEntity() {
    @Column(name = "email", nullable = false, updatable = false, length = 254)
    lateinit var email: String

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    lateinit var role: Role

    @Column(name = "verification_code", nullable = false, updatable = false, length = 6)
    @Pattern(regexp = "^[0-9]{6}$", message = "Verification code must be exactly 6 digits")
    lateinit var verificationCode: String

    @Column(name = "group_id", nullable = false, updatable = false)
    lateinit var groupId: UUID

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