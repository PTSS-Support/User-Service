package org.ptss.support.infrastructure.handlers.commands.invitations

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.interfaces.commands.invitations.IRegisterUserCommandHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import jakarta.ws.rs.BadRequestException
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@ApplicationScoped
class RegisterUserCommandHandler : IRegisterUserCommandHandler {

    override suspend fun handleAsync(command: RegisterUserCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: RegisterUserCommand): User {
        Log.debug("Attempting to register user with invitation code: ${command.invitationCode}")

        val invitation = InvitationEntity
            .find("verificationCode = ?1 and isVerified = true and isRegistered = false",
                command.invitationCode)
            .firstResult()
            ?: throw BadRequestException("Invalid or already used invitation code").also {
                Log.error("Invalid or already used invitation code: ${command.invitationCode}")
            }

        val user = UserEntity().apply {
            keycloakId = UUID.randomUUID() // TODO: This should be replaced with actual Keycloak integration
            firstName = command.firstName
            lastName = command.lastName
            role = invitation.role
        }

        user.persistAndFlush()

        invitation.isRegistered = true
        invitation.persistAndFlush()

        Log.info("Successfully registered user: ${user.firstName} ${user.lastName} with role: ${user.role}")
        return user.toModel()
    }
}