package org.ptss.support.domain.commands.invitations

import org.ptss.support.domain.enums.Role
import java.util.UUID

data class CreateInvitationCommand(
    val email: String,
    val role: Role,
    val groupId: UUID
)