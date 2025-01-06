package org.ptss.support.infrastructure.handlers.queries.invitations

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.invitations.GetPendingInvitationsByGroupQuery
import org.ptss.support.domain.interfaces.queries.invitations.IGetPendingInvitationsByGroupQueryHandler
import org.ptss.support.domain.models.Invitation
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import org.ptss.support.infrastructure.persistence.entities.toModel

@ApplicationScoped
class GetPendingInvitationsByGroupQueryHandler : IGetPendingInvitationsByGroupQueryHandler {
    override suspend fun handleAsync(query: GetPendingInvitationsByGroupQuery): List<Invitation> {
        Log.debug("Fetching pending invitations for group ID: ${query.groupId}")

        val invitations = InvitationEntity
            .find(
                "groupId = ?1 and isVerified = false and isRegistered = false and expiresAt > CURRENT_TIMESTAMP",
                query.groupId
            )
            .list()
            .map { it.toModel() }

        Log.debug("Retrieved ${invitations.size} pending invitations for group ${query.groupId}")
        return invitations
    }
}