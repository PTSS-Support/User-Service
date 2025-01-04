package org.ptss.support.infrastructure.handlers.commands.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.users.DeleteUserCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

@ApplicationScoped
class DeleteUserCommandHandler : ICommandHandler<DeleteUserCommand, Unit> {
    override suspend fun handleAsync(command: DeleteUserCommand): Unit {
        throw NotImplementedError("DeleteUserCommandHandler not implemented yet.")
    }
}