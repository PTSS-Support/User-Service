package org.ptss.support.infrastructure.handlers.queries.users

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetCurrentUserQuery
import org.ptss.support.domain.interfaces.queries.users.IGetCurrentUserQueryHandler
import org.ptss.support.domain.interfaces.queries.users.IGetUserByIdQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.domain.queries.users.GetUserByIdQuery

// This currently fully utilizes the functionality of the `GetUserByIdQueryHandler` but is kept separate for specific handling in the future
@ApplicationScoped
class GetCurrentUserQueryHandler(
    private val getUserByIdQueryHandler: IGetUserByIdQueryHandler
) : IGetCurrentUserQueryHandler {
    override suspend fun handleAsync(query: GetCurrentUserQuery): User {
        Log.debug("Fetching current user with ID: ${query.userId}")
        return getUserByIdQueryHandler.handleAsync(GetUserByIdQuery(query.userId)).also {
            Log.debug("Successfully retrieved current user: ${it.id}")
        }
    }
}