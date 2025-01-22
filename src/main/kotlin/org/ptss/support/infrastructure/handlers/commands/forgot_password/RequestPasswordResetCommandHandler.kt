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
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.responses.AuthIdentityResponse
import java.util.UUID

@ApplicationScoped
class RequestPasswordResetCommandHandler(
    private val authenticationServiceClient: IAuthenticationServiceClient
) : IRequestPasswordResetCommandHandler {
    override suspend fun handleAsync(command: RequestPasswordResetCommand): String {
        val identity = authenticationServiceClient.getIdentityByEmail(command.email)

        return withContext(Dispatchers.IO) {
            handleTransaction(command, identity)
        }
    }

    @Transactional
    fun handleTransaction(command: RequestPasswordResetCommand, identity: AuthIdentityResponse): String {
        Log.debug("Requesting password reset for email: ${command.email}")

        // Check if user exists and has appropriate role
        val user = UserEntity.find("keycloakId", identity.id)
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