package org.ptss.support.infrastructure.handlers.commands.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

@ApplicationScoped
class AssignPatientToGroupCommandHandler : ICommandHandler<AssignPatientToGroupCommand, Unit> {
    override suspend fun handleAsync(command: AssignPatientToGroupCommand): Unit {
        throw NotImplementedError("AssignPatientToGroupCommandHandler not implemented yet.")
    }
}