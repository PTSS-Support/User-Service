package org.ptss.support.api.dtos.responses.users

import java.util.UUID
import java.time.OffsetDateTime

data class UserResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val lastSeen: OffsetDateTime?,
    val groupId: UUID?
)