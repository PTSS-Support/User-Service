package org.ptss.support.api.dtos.responses.groups

import java.util.UUID

data class GroupResponse(
    val id: UUID,
    val patientId: UUID,
    val healthcareProfessionalId: UUID,
    val primaryCaregiverId: UUID?
)