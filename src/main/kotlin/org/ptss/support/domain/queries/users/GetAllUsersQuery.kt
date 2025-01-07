package org.ptss.support.domain.queries.users

import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import java.util.UUID

data class GetAllUsersQuery(
    val limit: Int = DEFAULT_LIMIT,
    val cursor: UUID?
)