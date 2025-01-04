package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.api.dtos.requests.toCommand
import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.facades.IInvitationFacade
import org.ptss.support.domain.models.User
import java.util.UUID

@ApplicationScoped
class InvitationFacade(
    private val createInvitationCommandHandler: ICommandHandler<CreateInvitationCommand, Unit>,
    private val verifyInvitationCommandHandler: ICommandHandler<VerifyInvitationCommand, Unit>,
    private val registerUserCommandHandler: ICommandHandler<RegisterUserCommand, User>,
    private val assignPatientToGroupCommandHandler: ICommandHandler<AssignPatientToGroupCommand, Unit>
) : IInvitationFacade {

    override suspend fun inviteUser(request: CreateInvitationRequest) {
        // TODO: Replace with actual group ID from context
        val groupId = UUID.randomUUID()
        val command = request.toCommand(groupId)
        createInvitationCommandHandler.handleAsync(command)
    }

    override suspend fun verifyInvitation(request: UserInvitationVerificationRequest) {
        val command = request.toCommand()
        verifyInvitationCommandHandler.handleAsync(command)
    }

    override suspend fun registerUser(request: UserRegistrationRequest) {
        // Register the user and get back the created user with role and group info
        val command = request.toCommand()
        val createdUser = registerUserCommandHandler.handleAsync(command)

        // If the registered user is a patient, assign them to their group
        if (createdUser.role == Role.PATIENT && createdUser.groupId != null) {
            val assignCommand = AssignPatientToGroupCommand(
                groupId = createdUser.groupId,
                patientId = createdUser.id
            )
            assignPatientToGroupCommandHandler.handleAsync(assignCommand)
        }
    }
}