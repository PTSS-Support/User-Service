package org.ptss.support.domain.interfaces.commands.invitations

import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface ICreateInvitationCommandHandler : ICommandHandler<CreateInvitationCommand, Unit>