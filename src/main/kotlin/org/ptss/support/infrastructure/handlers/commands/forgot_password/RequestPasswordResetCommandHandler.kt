package org.ptss.support.infrastructure.handlers.commands.forgot_password

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.ForbiddenException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.forgot_password.RequestPasswordResetCommand
import org.ptss.support.domain.interfaces.commands.forgot_password.IRequestPasswordResetCommandHandler
import org.ptss.support.infrastructure.persistence.entities.PasswordResetEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.domain.enums.Role
import java.util.UUID

@ApplicationScoped
class RequestPasswordResetCommandHandler : IRequestPasswordResetCommandHandler {
    override suspend fun handleAsync(command: RequestPasswordResetCommand): String =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: RequestPasswordResetCommand): String {
        Log.debug("Requesting password reset for email: ${command.email}")

        // TODO: In the future, this will be replaced with a call to the authentication service
        // to get the keycloakId associated with the email
        val keycloakId = UUID.fromString("6c29869b-fcd3-4604-a2f3-1d87057c7cbb")

        // Check if user exists and has appropriate role
        val user = UserEntity.find("keycloakId", keycloakId)
            .firstResult() ?: throw BadRequestException("Invalid email address")

        if (user.role !in setOf(Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER)) {
            throw ForbiddenException("Password reset not available for this user type")
        }

        val resetEntity = PasswordResetEntity().apply {
            this.email = command.email
            generateAndSetResetCode()
        }

        resetEntity.persistAndFlush()
        Log.info("Created password reset request for email: ${command.email}")
        return resetEntity.resetCode
    }
}