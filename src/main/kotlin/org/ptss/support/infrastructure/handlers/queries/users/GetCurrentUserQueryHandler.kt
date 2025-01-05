package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetCurrentUserQuery
import org.ptss.support.domain.interfaces.queries.users.IGetCurrentUserQueryHandler
import org.ptss.support.domain.models.User

@ApplicationScoped
class GetCurrentUserQueryHandler : IGetCurrentUserQueryHandler {
    override suspend fun handleAsync(query: GetCurrentUserQuery): User {
        throw NotImplementedError("GetCurrentUserQueryHandler not implemented yet.")
    }
}
