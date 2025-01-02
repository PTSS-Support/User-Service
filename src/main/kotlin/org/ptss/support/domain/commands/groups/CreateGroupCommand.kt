package org.ptss.support.domain.commands.groups

import java.util.UUID

data class CreateGroupCommand(
    val healthcareProfessionalId: UUID
)