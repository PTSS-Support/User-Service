package org.ptss.support.domain.interfaces.facades

import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import java.util.UUID

interface IUserFacade {
    suspend fun getAllUsers(limit: Int = DEFAULT_LIMIT, cursor: UUID?): CursorPage<UserResponse>

    suspend fun getCurrentUser(): UserResponse

    suspend fun getUserById(userId: UUID): UserResponse

    suspend fun deleteUser(userId: UUID)
}