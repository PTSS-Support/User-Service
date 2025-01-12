package org.ptss.support.domain.interfaces.commands.forgot_password

import org.ptss.support.domain.commands.forgot_password.RequestPasswordResetCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IRequestPasswordResetCommandHandler : ICommandHandler<RequestPasswordResetCommand, String>