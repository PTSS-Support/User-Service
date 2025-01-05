package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler

@ApplicationScoped
class CreateInvitationCommandHandler : ICreateInvitationCommandHandler {
    override suspend fun handleAsync(command: CreateInvitationCommand): Unit {
        throw NotImplementedError("CreateInvitationCommandHandler not implemented yet.")
    }
}