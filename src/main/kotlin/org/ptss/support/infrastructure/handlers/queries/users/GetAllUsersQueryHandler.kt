package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetAllUsersQuery
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.models.User

@ApplicationScoped
class GetAllUsersQueryHandler : IQueryHandler<GetAllUsersQuery, CursorPage<User>> {
    override suspend fun handleAsync(query: GetAllUsersQuery): CursorPage<User> {
        throw NotImplementedError("GetAllUsersQueryHandler not implemented yet.")
    }
}