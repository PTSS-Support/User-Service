package org.ptss.support.domain.queries.users

import java.util.UUID

data class GetCurrentUserQuery(
    val userId: UUID
)