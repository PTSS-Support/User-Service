package org.ptss.support.domain.queries.users

import java.util.UUID

data class GetAllUsersQuery(
    val limit: Int?,
    val cursor: UUID?
)