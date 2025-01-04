package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.interfaces.commands.invitations.IVerifyInvitationCommandHandler

@ApplicationScoped
class VerifyInvitationCommandHandler : IVerifyInvitationCommandHandler {
    override suspend fun handleAsync(command: VerifyInvitationCommand): Unit {
        throw NotImplementedError("VerifyInvitationCommandHandler not implemented yet.")
    }
}