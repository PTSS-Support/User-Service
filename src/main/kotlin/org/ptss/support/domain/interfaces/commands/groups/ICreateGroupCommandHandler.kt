package org.ptss.support.domain.interfaces.commands.groups

import org.ptss.support.domain.commands.groups.CreateGroupCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Group

interface ICreateGroupCommandHandler : ICommandHandler<CreateGroupCommand, Group>