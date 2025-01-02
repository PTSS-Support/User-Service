package org.ptss.support.domain.models

import java.util.UUID

data class Group(
    val id: UUID,
    val patientId: UUID?,
    val healthcareProfessionalId: UUID,
    val primaryCaregiverId: UUID?,
    val familyMembers: Set<GroupFamilyMember>,
    val isActive: Boolean = patientId != null
)