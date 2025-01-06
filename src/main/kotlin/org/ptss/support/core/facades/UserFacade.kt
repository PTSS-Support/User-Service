package org.ptss.support.core.facades

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.api.dtos.responses.toResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.common.pagination.mapItems
import org.ptss.support.domain.commands.users.DeleteUserCommand
import org.ptss.support.domain.interfaces.commands.users.IDeleteUserCommandHandler
import org.ptss.support.domain.interfaces.facades.IUserFacade
import org.ptss.support.domain.interfaces.queries.users.IGetAllUsersQueryHandler
import org.ptss.support.domain.interfaces.queries.users.IGetCurrentUserQueryHandler
import org.ptss.support.domain.interfaces.queries.users.IGetUserByIdQueryHandler
import org.ptss.support.domain.queries.users.GetAllUsersQuery
import org.ptss.support.domain.queries.users.GetCurrentUserQuery
import org.ptss.support.domain.queries.users.GetUserByIdQuery
import org.ptss.support.security.context.AuthenticatedUserContext
import java.util.UUID

@ApplicationScoped
class UserFacade(
    private val getAllUsersQueryHandler: IGetAllUsersQueryHandler,
    private val getCurrentUserQueryHandler: IGetCurrentUserQueryHandler,
    private val getUserByIdQueryHandler: IGetUserByIdQueryHandler,
    private val deleteUserCommandHandler: IDeleteUserCommandHandler,
    private val userContext: AuthenticatedUserContext
) : IUserFacade {

    override suspend fun getAllUsers(limit: Int, cursor: UUID?): CursorPage<UserResponse> {
        val query = GetAllUsersQuery(limit, cursor)
        return getAllUsersQueryHandler.handleAsync(query)
            .mapItems { it.toResponse() }
    }

    override suspend fun getCurrentUser(): UserResponse {
        val user = userContext.getCurrentUser()
        Log.info("Authenticated user $user with id ${user.userId}")
        val query = GetCurrentUserQuery(userId = user.userId)
        return getCurrentUserQueryHandler.handleAsync(query)
            .toResponse()
    }

    override suspend fun getUserById(userId: UUID): UserResponse {
        val query = GetUserByIdQuery(userId = userId)
        return getUserByIdQueryHandler.handleAsync(query)
            .toResponse()
    }

    override suspend fun deleteUser(userId: UUID) {
        val command = DeleteUserCommand(userId = userId)
        deleteUserCommandHandler.handleAsync(command)
    }
}