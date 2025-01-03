package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.facades.IUserFacade
import org.ptss.support.domain.interfaces.controllers.IUserController
import java.util.UUID

@ApplicationScoped
class UserController @Inject constructor(
    private val userFacade: IUserFacade
) : IUserController {

    override suspend fun getAllUsers(limit: Int?, cursor: UUID?): CursorPage<UserResponse> {
        return userFacade.getAllUsers(limit, cursor)
    }

    override suspend fun getCurrentUser(): UserResponse {
        return userFacade.getCurrentUser()
    }

    override suspend fun getUserById(id: UUID): UserResponse {
        return userFacade.getUserById(id)
    }

    override suspend fun deleteUser(id: UUID): Response {
        userFacade.deleteUser(id)
        return Response.noContent().build()
    }
}