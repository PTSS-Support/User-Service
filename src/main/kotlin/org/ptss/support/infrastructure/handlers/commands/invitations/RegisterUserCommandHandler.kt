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
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.AuthCreateIdentityRequest
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import java.util.UUID

@ApplicationScoped
class RegisterUserCommandHandler(
    private val authenticationServiceClient: IAuthenticationServiceClient
) : IRegisterUserCommandHandler {

    override suspend fun handleAsync(command: RegisterUserCommand): User {
        // First validate the invitation
        val invitation = withContext(Dispatchers.IO) {
            validateInvitation(command.invitationCode)
        }

        val userId = UUID.randomUUID()

        // Create identity in authentication service
        Log.info("""Calling identity service client with request: 
            userId: $userId
            email: ${invitation.email}
            password: ${command.password}
            role: ${invitation.role}
            groupId: ${invitation.groupId}
            firstName: ${command.firstName}
            lastName: ${command.lastName}
        """.trimMargin())
        val identity = executeWithExceptionLoggingAsync(
            operation = {
                authenticationServiceClient.createIdentity(
                    AuthCreateIdentityRequest(
                        userId = userId.toString(),
                        email = invitation.email,
                        password = command.password,
                        role = invitation.role,
                        groupId = invitation.groupId.toString(),
                        firstName = command.firstName,
                        lastName = command.lastName
                    )
                )
            },
            logMessage = "Failed to create identity in authentication service"
        )

        // Create user in our database
        return withContext(Dispatchers.IO) {
            createUser(command, invitation, userId, identity.id)
        }
    }

    @Transactional
    fun validateInvitation(invitationCode: String): InvitationEntity {
        return InvitationEntity
            .find("verificationCode = ?1 and isVerified = true and isRegistered = false",
                invitationCode)
            .firstResult()
            ?: throw BadRequestException("Invalid or already used invitation code").also {
                Log.error("Invalid or already used invitation code: $invitationCode")
            }
    }

    @Transactional
    fun createUser(
        command: RegisterUserCommand,
        invitation: InvitationEntity,
        userId: UUID,
        keycloakId: String
    ): User {
        val user = UserEntity().apply {
            id = userId
            this.keycloakId = UUID.fromString(keycloakId)
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