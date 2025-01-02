package org.ptss.support.api.dtos.responses.invitations

import org.ptss.support.domain.enums.Role
import java.util.UUID

data class InvitationResponse(
    val email: String,
    val role: Role,
    val groupId: UUID,
    val isRegistered: Boolean
)