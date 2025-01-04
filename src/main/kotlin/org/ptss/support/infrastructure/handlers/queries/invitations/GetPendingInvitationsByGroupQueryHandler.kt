package org.ptss.support.infrastructure.handlers.queries.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.invitations.GetPendingInvitationsByGroupQuery
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Invitation

@ApplicationScoped
class GetPendingInvitationsByGroupQueryHandler : IQueryHandler<GetPendingInvitationsByGroupQuery, List<Invitation>> {
    override suspend fun handleAsync(query: GetPendingInvitationsByGroupQuery): List<Invitation> {
        throw NotImplementedError("GetPendingInvitationsByGroupQueryHandler not implemented yet.")
    }
}