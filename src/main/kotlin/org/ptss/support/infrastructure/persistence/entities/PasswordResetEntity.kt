package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanionBase
import jakarta.persistence.*
import org.ptss.support.domain.config.ForgotPasswordProperties
import java.time.OffsetDateTime
import java.util.UUID
import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.RESET_CODE_LENGTH

@Entity
@Table(name = "password_resets")
class PasswordResetEntity : BaseEntity() {
    @Column(nullable = false, length = EMAIL_MAX_LENGTH)
    lateinit var email: String

    @Column(nullable = false)
    lateinit var resetCode: String

    @Column(nullable = false)
    lateinit var expiresAt: OffsetDateTime

    @Column(nullable = false)
    var isVerified: Boolean = false

    @Column(nullable = false)
    var isUsed: Boolean = false

    companion object : PanacheCompanionBase<PasswordResetEntity, UUID> {
        fun findValidResetCode(email: String, resetCode: String): PasswordResetEntity? {
            return find(
                "email = ?1 and resetCode = ?2 and expiresAt > ?3 and isUsed = false",
                email, resetCode, OffsetDateTime.now()
            ).firstResult()
        }
    }

    @PrePersist
    fun prePersist() {
        if (!::expiresAt.isInitialized) {
            expiresAt = OffsetDateTime.now().plusMinutes(ForgotPasswordProperties.validityHours)
        }
    }

    fun generateAndSetResetCode(): String {
        resetCode = buildString {
            repeat(RESET_CODE_LENGTH) {
                append((0..9).random())
            }
        }
        return resetCode
    }
}