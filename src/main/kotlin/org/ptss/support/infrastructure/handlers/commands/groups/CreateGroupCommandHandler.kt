package org.ptss.support.infrastructure.handlers.commands.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.groups.CreateGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.ICreateGroupCommandHandler
import org.ptss.support.domain.models.Group

@ApplicationScoped
class CreateGroupCommandHandler : ICreateGroupCommandHandler {
    override suspend fun handleAsync(command: CreateGroupCommand): Group {
        throw NotImplementedError("CreateGroupCommandHandler not implemented yet.")
    }
}