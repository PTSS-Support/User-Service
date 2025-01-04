package org.ptss.support.domain.interfaces.commands.invitations

import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.User

interface IRegisterUserCommandHandler : ICommandHandler<RegisterUserCommand, User>