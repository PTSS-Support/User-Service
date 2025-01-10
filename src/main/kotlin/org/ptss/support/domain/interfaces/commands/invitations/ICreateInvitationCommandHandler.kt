package org.ptss.support.domain.interfaces.commands.invitations

import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Invitation

interface ICreateInvitationCommandHandler : ICommandHandler<CreateInvitationCommand, Invitation>