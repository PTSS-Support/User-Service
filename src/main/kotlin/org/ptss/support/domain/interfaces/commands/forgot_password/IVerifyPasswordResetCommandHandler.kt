package org.ptss.support.domain.interfaces.commands.forgot_password

import org.ptss.support.domain.commands.forgot_password.VerifyPasswordResetCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler

interface IVerifyPasswordResetCommandHandler : ICommandHandler<VerifyPasswordResetCommand, Unit>