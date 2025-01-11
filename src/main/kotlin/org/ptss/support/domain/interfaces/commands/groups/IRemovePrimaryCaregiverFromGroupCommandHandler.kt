package org.ptss.support.domain.interfaces.commands.groups

import org.ptss.support.domain.commands.groups.RemovePrimaryCaregiverFromGroupCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IRemovePrimaryCaregiverFromGroupCommandHandler : ICommandHandler<RemovePrimaryCaregiverFromGroupCommand, Unit>