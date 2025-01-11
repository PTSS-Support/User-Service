package org.ptss.support.domain.commands.groups

import java.util.UUID

data class AssignPrimaryCaregiverToGroupCommand(
    val groupId: UUID,
    val memberId: UUID
)