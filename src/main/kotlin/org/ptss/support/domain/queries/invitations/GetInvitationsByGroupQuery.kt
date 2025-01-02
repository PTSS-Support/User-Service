package org.ptss.support.domain.queries.invitations

import java.util.UUID

data class GetInvitationsByGroupQuery(
    val groupId: UUID
)