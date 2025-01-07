package org.ptss.support.domain.queries.groups

import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import java.util.UUID

data class GetAllGroupsQuery(
    val limit: Int = DEFAULT_LIMIT,
    val cursor: UUID?
)
