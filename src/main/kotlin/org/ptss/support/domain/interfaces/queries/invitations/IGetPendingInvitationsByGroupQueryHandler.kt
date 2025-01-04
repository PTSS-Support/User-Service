package org.ptss.support.domain.interfaces.queries.invitations

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Invitation
import org.ptss.support.domain.queries.invitations.GetPendingInvitationsByGroupQuery

interface IGetPendingInvitationsByGroupQueryHandler : IQueryHandler<GetPendingInvitationsByGroupQuery, List<Invitation>>