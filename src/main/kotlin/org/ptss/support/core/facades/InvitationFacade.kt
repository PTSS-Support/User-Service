package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.api.dtos.requests.toCommand
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.facades.IInvitationFacade
import java.util.UUID

@ApplicationScoped
class InvitationFacade(
    private val createInvitationCommandHandler: ICommandHandler<CreateInvitationCommand, Unit>,
    private val verifyInvitationCommandHandler: ICommandHandler<VerifyInvitationCommand, Unit>,
    private val registerUserCommandHandler: ICommandHandler<RegisterUserCommand, Unit>
) : IInvitationFacade {

    override suspend fun inviteUser(request: CreateInvitationRequest) {
        // TODO: Replace with actual group ID from context
        val groupId = UUID.randomUUID()
        val command = request.toCommand(groupId)
        createInvitationCommandHandler.handleAsync(command)
    }

    override suspend fun verifyInvitation(request: UserInvitationVerificationRequest) {
        val command = request.toCommand()
        verifyInvitationCommandHandler.handleAsync(command)
    }

    override suspend fun registerUser(request: UserRegistrationRequest) {
        val command = request.toCommand()
        registerUserCommandHandler.handleAsync(command)
    }
}