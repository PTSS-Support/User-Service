package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetUserByIdQuery
import org.ptss.support.domain.interfaces.queries.users.IGetUserByIdQueryHandler
import org.ptss.support.domain.models.User

@ApplicationScoped
class GetUserByIdQueryHandler : IGetUserByIdQueryHandler {
    override suspend fun handleAsync(query: GetUserByIdQuery): User {
        throw NotImplementedError("GetUserByIdQueryHandler not implemented yet.")
    }
}