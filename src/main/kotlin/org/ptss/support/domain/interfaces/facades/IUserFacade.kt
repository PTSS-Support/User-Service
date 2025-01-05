package org.ptss.support.domain.interfaces.facades

import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import java.util.UUID

interface IUserFacade {
    suspend fun getAllUsers(limit: Int?, cursor: UUID?): CursorPage<UserResponse>

    suspend fun getCurrentUser(): UserResponse

    suspend fun getUserById(userId: UUID): UserResponse

    suspend fun deleteUser(userId: UUID)
}