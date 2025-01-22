package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.facades.IUserFacade
import org.ptss.support.domain.interfaces.controllers.IUserController
import org.ptss.support.security.Authentication
import java.util.UUID

@ApplicationScoped
class UserController @Inject constructor(
    private val userFacade: IUserFacade
) : IUserController {

    @Authentication(roles = [Role.ADMIN])
    override suspend fun getAllUsers(limit: Int, cursor: UUID?): CursorPage<UserResponse> {
        return userFacade.getAllUsers(limit, cursor)
    }

    @Authentication(roles = [Role.ADMIN, Role.HEALTHCARE_PROFESSIONAL, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun getCurrentUser(): UserResponse {
        return userFacade.getCurrentUser()
    }

    @Authentication(roles = [Role.ADMIN, Role.HEALTHCARE_PROFESSIONAL])
    override suspend fun getUserById(id: UUID): UserResponse {
        return userFacade.getUserById(id)
    }

    @Authentication(roles = [Role.ADMIN, Role.HEALTHCARE_PROFESSIONAL, Role.PATIENT, Role.PRIMARY_CAREGIVER])
    override suspend fun deleteUser(id: UUID): Response {
        userFacade.deleteUser(id)
        return Response.noContent().build()
    }
}