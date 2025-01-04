package org.ptss.support.infrastructure.handlers.queries.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupMembersQueryHandler
import org.ptss.support.domain.models.User

@ApplicationScoped
class GetGroupMembersQueryHandler : IGetGroupMembersQueryHandler {
    override suspend fun handleAsync(query: GetGroupMembersQuery): List<User> {
        throw NotImplementedError("GetGroupMembersQueryHandler not implemented yet.")
    }
}