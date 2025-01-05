package org.ptss.support.domain.queries.groups

import java.util.UUID

data class GetAllGroupsQuery(
    val limit: Int?,
    val cursor: UUID?
)
