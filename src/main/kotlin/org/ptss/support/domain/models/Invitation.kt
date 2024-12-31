package org.ptss.support.domain.models

import org.ptss.support.domain.constants.ValidationMessages.UNINVITABLE_ROLE
import org.ptss.support.domain.enums.Role
import java.time.OffsetDateTime
import java.util.UUID

data class Invitation(
    val id: UUID,
    val email: String,
    val role: Role,
    val verificationCode: String,
    val groupId: UUID,
    val expiresAt: OffsetDateTime,
    val isVerified: Boolean,
    val isRegistered: Boolean
) {
    init {
        require(role.canBeInvited()) {
            UNINVITABLE_ROLE
        }
    }
}