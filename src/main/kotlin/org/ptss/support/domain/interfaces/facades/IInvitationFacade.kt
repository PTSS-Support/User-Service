package org.ptss.support.domain.interfaces.facades

import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest

interface IInvitationFacade {
    suspend fun inviteUser(request: CreateInvitationRequest)

    suspend fun verifyInvitation(request: UserInvitationVerificationRequest)

    suspend fun registerUser(request: UserRegistrationRequest)
}