package org.ptss.support.domain.models

import java.util.UUID
import java.time.OffsetDateTime

data class User(
    val id: UUID,
    val keycloakId: String,
    val firstName: String,
    val lastName: String,
    val lastSeen: OffsetDateTime?,
    val groupId: UUID?
)