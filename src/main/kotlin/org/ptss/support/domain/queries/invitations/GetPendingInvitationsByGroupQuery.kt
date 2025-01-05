package org.ptss.support.domain.queries.invitations

import java.util.UUID

data class GetPendingInvitationsByGroupQuery(
    val groupId: UUID
)