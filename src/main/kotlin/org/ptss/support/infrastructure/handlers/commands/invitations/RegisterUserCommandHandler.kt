package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.interfaces.commands.invitations.IRegisterUserCommandHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import jakarta.ws.rs.BadRequestException
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.util.UUID

@ApplicationScoped
class RegisterUserCommandHandler(
    private val em: EntityManager
) : IRegisterUserCommandHandler {

    @Transactional
    override suspend fun handleAsync(command: RegisterUserCommand): User {
        val invitation = InvitationEntity
            .find("verificationCode = ?1 and isVerified = true and isRegistered = false",
                command.invitationCode)
            .firstResult()
            ?: throw BadRequestException("Invalid or already used invitation code")

        val user = UserEntity().apply {
            keycloakId = UUID.randomUUID() // TODO: This should be replaced with actual Keycloak integration
            firstName = command.firstName
            lastName = command.lastName
            role = invitation.role
        }

        user.persistAndFlush()

        // Mark invitation as registered
        invitation.isRegistered = true
        invitation.persistAndFlush()

        return user.toModel()
    }
}