package org.ptss.support.infrastructure.handlers.commands.forgot_password

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.forgot_password.ResetPasswordCommand
import org.ptss.support.domain.interfaces.commands.forgot_password.IResetPasswordCommandHandler
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.AuthResetPasswordRequest
import org.ptss.support.infrastructure.persistence.entities.PasswordResetEntity
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import java.time.OffsetDateTime

@ApplicationScoped
class ResetPasswordCommandHandler(
    private val authenticationServiceClient: IAuthenticationServiceClient
) : IResetPasswordCommandHandler {
    override suspend fun handleAsync(command: ResetPasswordCommand) {
        // First validate and get the reset entity in a transaction
        val resetEntity = withContext(Dispatchers.IO) {
            validateAndGetResetEntity(command)
        }

        executeWithExceptionLoggingAsync(
            operation = {
                // Then call auth service to reset the password
                authenticationServiceClient.resetPassword(
                    AuthResetPasswordRequest(
                        email = resetEntity.email,
                        newPassword = command.newPassword
                    )
                )

                // If successful, mark the reset code as used in a new transaction
                withContext(Dispatchers.IO) {
                    markResetCodeAsUsed(resetEntity)
                }

                Log.info("Successfully reset password for email: ${resetEntity.email}")
            },
            logMessage = "Failed to reset password with authentication service"
        )
    }

    @Transactional
    fun validateAndGetResetEntity(command: ResetPasswordCommand): PasswordResetEntity {
        Log.debug("Validating reset code")

        val resetEntity = PasswordResetEntity.find("resetCode", command.resetCode)
            .firstResult() ?: throw BadRequestException("Invalid reset code")

        if (!resetEntity.isVerified || resetEntity.isUsed || resetEntity.expiresAt <= OffsetDateTime.now()) {
            throw BadRequestException("Reset code has expired, is unverified, or has already been used")
        }

        return resetEntity
    }

    @Transactional
    fun markResetCodeAsUsed(resetEntity: PasswordResetEntity) {
        resetEntity.isUsed = true
        resetEntity.persistAndFlush()
    }
}