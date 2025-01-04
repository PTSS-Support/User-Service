package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.User

@ApplicationScoped
class RegisterUserCommandHandler : ICommandHandler<RegisterUserCommand, User> {
    override suspend fun handleAsync(command: RegisterUserCommand): User {
        throw NotImplementedError("RegisterUserCommandHandler not implemented yet.")
    }
}