package org.ptss.support.infrastructure.handlers.commands.forgot_password

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.forgot_password.VerifyPasswordResetCommand
import org.ptss.support.domain.interfaces.commands.forgot_password.IVerifyPasswordResetCommandHandler
import org.ptss.support.infrastructure.persistence.entities.PasswordResetEntity

@ApplicationScoped
class VerifyPasswordResetCommandHandler : IVerifyPasswordResetCommandHandler {
    override suspend fun handleAsync(command: VerifyPasswordResetCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: VerifyPasswordResetCommand) {
        Log.debug("Verifying password reset code for email: ${command.email}")

        val passwordReset = PasswordResetEntity.findValidResetCode(command.email, command.resetCode)
            ?: throw BadRequestException("Invalid or expired reset code").also {
                Log.error("Invalid verification attempt for email: ${command.email}")
            }

        passwordReset.isVerified = true
        passwordReset.persistAndFlush()
        Log.info("Successfully verified reset code for email: ${command.email}")
    }
}