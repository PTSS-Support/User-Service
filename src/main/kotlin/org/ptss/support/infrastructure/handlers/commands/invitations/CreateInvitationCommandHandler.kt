package org.ptss.support.infrastructure.handlers.commands.invitations

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import jakarta.ws.rs.BadRequestException

@ApplicationScoped
class CreateInvitationCommandHandler : ICreateInvitationCommandHandler {
    override suspend fun handleAsync(command: CreateInvitationCommand) {
        // Check if an invitation already exists for this email
        val existingInvitation = InvitationEntity.find("email", command.email).firstResult()
        if (existingInvitation != null && !existingInvitation.isRegistered) {
            throw BadRequestException("An active invitation already exists for this email")
        }

        val invitation = InvitationEntity().apply {
            email = command.email
            role = command.role
            groupId = command.groupId
        }

        invitation.persistAndFlush()
    }
}