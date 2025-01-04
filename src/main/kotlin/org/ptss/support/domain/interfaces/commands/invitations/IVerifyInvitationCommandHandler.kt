package org.ptss.support.domain.interfaces.commands.invitations

import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IVerifyInvitationCommandHandler : ICommandHandler<VerifyInvitationCommand, Unit>