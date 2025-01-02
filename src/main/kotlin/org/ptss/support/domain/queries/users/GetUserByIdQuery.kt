package org.ptss.support.domain.queries.users

import java.util.UUID

data class GetUserByIdQuery(
    val userId: UUID
)