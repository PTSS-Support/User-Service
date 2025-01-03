package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.domain.interfaces.facades.IInvitationFacade
import org.ptss.support.domain.interfaces.controllers.IInvitationController

@ApplicationScoped
class InvitationController @Inject constructor(
    private val invitationFacade: IInvitationFacade
) : IInvitationController {

    override suspend fun inviteUser(request: CreateInvitationRequest): Response {
        invitationFacade.inviteUser(request)
        return Response.status(Response.Status.CREATED).build()
    }

    override suspend fun verifyInvitation(request: UserInvitationVerificationRequest): Response {
        invitationFacade.verifyInvitation(request)
        return Response.noContent().build()
    }

    override suspend fun registerUser(request: UserRegistrationRequest): Response {
        invitationFacade.registerUser(request)
        return Response.status(Response.Status.CREATED).build()
    }
}