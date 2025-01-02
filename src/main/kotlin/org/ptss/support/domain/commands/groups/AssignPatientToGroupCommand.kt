package org.ptss.support.domain.commands.groups

import java.util.UUID

data class AssignPatientToGroupCommand(
    val groupId: UUID,
    val patientId: UUID
)