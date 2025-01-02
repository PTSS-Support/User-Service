package org.ptss.support.domain.models

import java.time.OffsetDateTime;
import java.util.UUID;

data class GroupFamilyMember(
    val id: UUID,
    val groupId: UUID,
    val userId: UUID,
    val anonymizedAt:OffsetDateTime?
)
