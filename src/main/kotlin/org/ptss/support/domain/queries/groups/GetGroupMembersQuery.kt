package org.ptss.support.domain.queries.groups

import java.util.UUID

data class GetGroupMembersQuery(
    val groupId: UUID
)