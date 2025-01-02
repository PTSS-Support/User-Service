package org.ptss.support.domain.queries.groups

import java.util.UUID

data class GetGroupByIdQuery(
    val groupId: UUID
)