package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.requests.auth.*
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.facades.IAuthFacade
import org.ptss.support.domain.interfaces.controllers.IAuthController
import org.ptss.support.security.Authentication

@ApplicationScoped
class AuthController @Inject constructor(
    private val authFacade: IAuthFacade
) : IAuthController {

    override suspend fun login(request: LoginRequest): Response {
        val cookies = authFacade.login(request)
        return Response.ok().cookie(*cookies).build()
    }

    override suspend fun loginWithPin(request: PinLoginRequest): Response {
        val cookies = authFacade.loginWithPin(request)
        return Response.ok().cookie(*cookies).build()
    }

    @Authentication(roles = [Role.ADMIN, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER, Role.HCP])
    override suspend fun logout(): Response {
        authFacade.logout()
        return Response.noContent().build()
    }

    @Authentication(roles = [Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun createPin(request: PinCreateRequest): Response {
        authFacade.createPin(request)
        return Response.status(Response.Status.CREATED).build()
    }

    @Authentication(roles = [Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun updatePin(request: PinUpdateRequest): Response {
        authFacade.updatePin(request)
        return Response.ok().build()
    }

    @Authentication(roles = [Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun updatePassword(request: PasswordUpdateRequest): Response {
        authFacade.updatePassword(request)
        return Response.ok().build()
    }
}