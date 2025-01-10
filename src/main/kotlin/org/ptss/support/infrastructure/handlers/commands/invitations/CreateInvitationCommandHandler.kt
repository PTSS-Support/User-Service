package org.ptss.support.infrastructure.handlers.commands.invitations

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import jakarta.ws.rs.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.models.Invitation
import org.ptss.support.infrastructure.persistence.entities.toModel

@ApplicationScoped
class CreateInvitationCommandHandler : ICreateInvitationCommandHandler {

    override suspend fun handleAsync(command: CreateInvitationCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: CreateInvitationCommand): Invitation {
        Log.debug("Attempting to create invitation for email: ${command.email}")

        val existingInvitation = InvitationEntity.find("email", command.email).firstResult()
        if (existingInvitation != null && !existingInvitation.isRegistered) {
            Log.warn("Attempted to create duplicate invitation for email: ${command.email}")
            throw BadRequestException("An active invitation already exists for this email")
        }

        val invitation = InvitationEntity().apply {
            email = command.email
            role = command.role
            groupId = command.groupId
            generateAndSetVerificationCode()
        }

        invitation.persistAndFlush()
        Log.info("Successfully created invitation for email: ${command.email} with role: ${command.role}")
        return invitation.toModel()
    }
}