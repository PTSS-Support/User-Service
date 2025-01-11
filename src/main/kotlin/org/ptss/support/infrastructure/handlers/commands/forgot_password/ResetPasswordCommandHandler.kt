package org.ptss.support.infrastructure.handlers.commands.forgot_password

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.forgot_password.ResetPasswordCommand
import org.ptss.support.domain.interfaces.commands.forgot_password.IResetPasswordCommandHandler
import org.ptss.support.infrastructure.persistence.entities.PasswordResetEntity
import java.time.OffsetDateTime

@ApplicationScoped
class ResetPasswordCommandHandler : IResetPasswordCommandHandler {
    override suspend fun handleAsync(command: ResetPasswordCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: ResetPasswordCommand) {
        Log.debug("Attempting to reset password")

        val resetEntity = PasswordResetEntity.find("resetCode", command.resetCode)
            .firstResult() ?: throw BadRequestException("Invalid reset code")

        if (!resetEntity.isVerified || resetEntity.isUsed || resetEntity.expiresAt <= OffsetDateTime.now()) {
            throw BadRequestException("Reset code has expired, is unverified, or has already been used")
        }

        // TODO: In the future, this will call the authentication service to update the password
        // For now, we just mark the reset code as used
        resetEntity.isUsed = true
        resetEntity.persistAndFlush()

        Log.info("Successfully reset password for email: ${resetEntity.email}")
    }
}