package org.ptss.support.infrastructure.handlers.queries.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupUsersQueryHandler
import org.ptss.support.domain.models.User

@ApplicationScoped
class GetGroupUsersQueryHandler : IGetGroupUsersQueryHandler {
    override suspend fun handleAsync(query: GetGroupUsersQuery): List<User> {
        throw NotImplementedError("GetGroupUsersQueryHandler not implemented yet.")
    }
}