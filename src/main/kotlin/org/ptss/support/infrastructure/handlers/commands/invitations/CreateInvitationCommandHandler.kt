package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

@ApplicationScoped
class CreateInvitationCommandHandler : ICommandHandler<CreateInvitationCommand, Unit> {
    override suspend fun handleAsync(command: CreateInvitationCommand): Unit {
        throw NotImplementedError("CreateInvitationCommandHandler not implemented yet.")
    }
}