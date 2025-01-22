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

        // Create initial user without keycloakId
        val initialUser = withContext(Dispatchers.IO) {
            createInitialUser(command, command.invitationCode)
        }

        // Then create identity in authentication service using the generated user ID
        Log.info("""Calling identity service client with request: 
            userId: ${initialUser.id}
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
                        userId = initialUser.id.toString(),
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

        // Update the user with the keycloak ID in a new transaction
        Log.info("Updating keycloakId ${identity.id} for user ${initialUser.id}}")
        val finalUser = withContext(Dispatchers.IO) {
            finalizeUser(initialUser.id, UUID.fromString(identity.id))
        }

        // Verify the user is completely finalized
        val verifiedUser = withContext(Dispatchers.IO) {
            verifyUserCreation(finalUser.id)
        }

        return verifiedUser
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
    fun createInitialUser(
        command: RegisterUserCommand,
        invitationCode: String,
    ): User {
        // Reload the invitation in this transaction
        val invitation = InvitationEntity
            .find("verificationCode", invitationCode)
            .firstResult()
            ?: throw BadRequestException("Invalid invitation code")

        val user = UserEntity().apply {
            firstName = command.firstName
            lastName = command.lastName
            role = invitation.role
            // keycloakId is null at this point
        }

        user.persistAndFlush()

        // Update the invitation in the same transaction where we loaded it
        invitation.isRegistered = true
        invitation.persistAndFlush()

        Log.info("Successfully created initial user: ${user.firstName} ${user.lastName} with role: ${user.role}")
        return user.toModel()
    }

    @Transactional
    fun finalizeUser(userId: UUID, keycloakId: UUID): User {
        val user = UserEntity.findById(userId)
            ?: throw IllegalStateException("User not found")

        user.keycloakId = keycloakId
        user.persistAndFlush()

        return user.toModel()
    }

    @Transactional
    fun verifyUserCreation(userId: UUID): User {
        val user = UserEntity.findById(userId)
            ?: throw IllegalStateException("User not found")

        if (user.keycloakId == null) {
            throw IllegalStateException("User creation failed - keycloakId is null")
        }

        return user.toModel()
    }
}