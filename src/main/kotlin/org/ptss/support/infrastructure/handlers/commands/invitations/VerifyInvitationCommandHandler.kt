package org.ptss.support.infrastructure.handlers.commands.invitations

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.interfaces.commands.invitations.IVerifyInvitationCommandHandler
import org.ptss.support.infrastructure.persistence.entities.InvitationEntity
import jakarta.ws.rs.BadRequestException
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class VerifyInvitationCommandHandler(
    private val em: EntityManager
) : IVerifyInvitationCommandHandler {

    @Transactional
    override suspend fun handleAsync(command: VerifyInvitationCommand) {
        Log.debug("Attempting to verify invitation for email: ${command.email}")

        val invitation = InvitationEntity.findByEmailAndVerificationCode(command.email, command.verificationCode)
            ?: throw BadRequestException("Invalid verification code or expired invitation").also {
                Log.error("Invalid verification attempt for email: ${command.email}")
            }

        invitation.isVerified = true
        invitation.persistAndFlush()
        Log.info("Successfully verified invitation for email: ${command.email}")
    }
}