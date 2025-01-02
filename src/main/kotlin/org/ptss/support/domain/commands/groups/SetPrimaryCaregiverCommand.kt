package org.ptss.support.domain.commands.groups

import java.util.UUID

data class SetPrimaryCaregiverCommand(
    val groupId: UUID,
    val familyMemberId: UUID
)