package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

@ApplicationScoped
class VerifyInvitationCommandHandler : ICommandHandler<VerifyInvitationCommand, Unit> {
    override suspend fun handleAsync(command: VerifyInvitationCommand): Unit {
        throw NotImplementedError("VerifyInvitationCommandHandler not implemented yet.")
    }
}