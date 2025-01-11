package org.ptss.support.domain.interfaces.commands.forgot_password

import org.ptss.support.domain.commands.forgot_password.ResetPasswordCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IResetPasswordCommandHandler : ICommandHandler<ResetPasswordCommand, Unit>