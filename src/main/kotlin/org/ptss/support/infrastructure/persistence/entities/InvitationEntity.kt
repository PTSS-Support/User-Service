package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import java.time.OffsetDateTime
import java.util.UUID
import jakarta.persistence.*
import org.ptss.support.domain.config.InvitationProperties
import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.VERIFICATION_CODE_LENGTH
import org.ptss.support.domain.enums.Role

@Entity
@Table(
    name = "invitations",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
class InvitationEntity : BaseEntity() {
    @Column(nullable = false, updatable = false, length = EMAIL_MAX_LENGTH)
    lateinit var email: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    lateinit var role: Role

    @Column(nullable = false, updatable = false, length = VERIFICATION_CODE_LENGTH)
    lateinit var verificationCode: String

    @Column(nullable = false, updatable = false)
    lateinit var groupId: UUID

    @Column(nullable = false)
    lateinit var expiresAt: OffsetDateTime

    @Column(nullable = false)
    var isVerified: Boolean = false

    @Column(nullable = false)
    var isRegistered: Boolean = false

    companion object : PanacheCompanion<InvitationEntity> {
        fun findByEmailAndVerificationCode(email: String, code: String): InvitationEntity? =
            find("email = ?1 and verificationCode = ?2 and isVerified = false and expiresAt > CURRENT_TIMESTAMP",
                email, code).firstResult()
    }

    @PrePersist
    fun prePersist() {
        if (!::verificationCode.isInitialized) {
            verificationCode = generateVerificationCode()
        }
        if (!::expiresAt.isInitialized) {
            expiresAt = OffsetDateTime.now().plusHours(InvitationProperties.validityHours)
        }
    }

    private fun generateVerificationCode(): String {
        return buildString {
            repeat(VERIFICATION_CODE_LENGTH) {
                append((0..9).random())
            }
        }
    }
}