package org.ptss.support.domain.interfaces.commands.users

import org.ptss.support.domain.commands.users.DeleteUserCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IDeleteUserCommandHandler : ICommandHandler<DeleteUserCommand, Unit>