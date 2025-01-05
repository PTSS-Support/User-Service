package org.ptss.support.domain.interfaces.commands.groups

import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IAssignPatientToGroupCommandHandler : ICommandHandler<AssignPatientToGroupCommand, Unit>