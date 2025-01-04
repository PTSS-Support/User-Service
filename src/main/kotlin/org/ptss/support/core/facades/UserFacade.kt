package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.commands.users.DeleteUserCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.facades.IUserFacade
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.queries.users.GetAllUsersQuery
import org.ptss.support.domain.queries.users.GetCurrentUserQuery
import org.ptss.support.domain.queries.users.GetUserByIdQuery
import java.util.UUID

@ApplicationScoped
class UserFacade(
    private val getAllUsersQueryHandler: IQueryHandler<GetAllUsersQuery, CursorPage<UserResponse>>,
    private val getCurrentUserQueryHandler: IQueryHandler<GetCurrentUserQuery, UserResponse>,
    private val getUserByIdQueryHandler: IQueryHandler<GetUserByIdQuery, UserResponse>,
    private val deleteUserCommandHandler: ICommandHandler<DeleteUserCommand, Unit>
) : IUserFacade {

    override suspend fun getAllUsers(limit: Int?, cursor: UUID?): CursorPage<UserResponse> {
        val query = GetAllUsersQuery(
            limit = limit,
            cursor = cursor
        )
        return getAllUsersQueryHandler.handleAsync(query)
    }

    override suspend fun getCurrentUser(): UserResponse {
        // TODO: Replace with actual user ID from context
        val userId = UUID.randomUUID()
        val query = GetCurrentUserQuery(userId = userId)
        return getCurrentUserQueryHandler.handleAsync(query)
    }

    override suspend fun getUserById(userId: UUID): UserResponse {
        val query = GetUserByIdQuery(userId = userId)
        return getUserByIdQueryHandler.handleAsync(query)
    }

    override suspend fun deleteUser(userId: UUID) {
        val command = DeleteUserCommand(userId = userId)
        deleteUserCommandHandler.handleAsync(command)
    }
}