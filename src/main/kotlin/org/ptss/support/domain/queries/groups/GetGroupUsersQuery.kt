package org.ptss.support.domain.queries.groups

import java.util.UUID

data class GetGroupUsersQuery(
    val groupId: UUID
)