package org.ptss.support.domain.models

import java.util.UUID
import java.time.OffsetDateTime

data class Group(
    val id: UUID,
    val patientId: UUID,
    val healthcareProfessionalId: UUID,
    val primaryCaregiverId: UUID?
)