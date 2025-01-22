package org.ptss.support.core.facades

import io.quarkus.logging.Log
import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.api.dtos.requests.toCommand
import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.constants.SecurityMessages.UNAUTHORIZED_ACCESS
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.commands.groups.IAssignPatientToGroupCommandHandler
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler
import org.ptss.support.domain.interfaces.commands.invitations.IRegisterUserCommandHandler
import org.ptss.support.domain.interfaces.commands.invitations.IVerifyInvitationCommandHandler
import org.ptss.support.domain.interfaces.facades.IInvitationFacade
import org.ptss.support.domain.interfaces.services.IEmailService
import org.ptss.support.domain.templates.EmailTemplates
import org.ptss.support.security.context.AuthenticatedUserContext

@ApplicationScoped
class InvitationFacade(
    private val createInvitationCommandHandler: ICreateInvitationCommandHandler,
    private val verifyInvitationCommandHandler: IVerifyInvitationCommandHandler,
    private val registerUserCommandHandler: IRegisterUserCommandHandler,
    private val assignPatientToGroupCommandHandler: IAssignPatientToGroupCommandHandler,
    private val emailService: IEmailService,
    private val userContext: AuthenticatedUserContext
) : IInvitationFacade {

    override suspend fun inviteUser(request: CreateInvitationRequest) {
        val user = userContext.getCurrentUser()
        Log.info("Authenticated user $user with groupId ${user.groupId?: "ILLEGAL STATE"}")
        val command = request.toCommand(user.groupId?: throw UnauthorizedException(UNAUTHORIZED_ACCESS))
        val invitation = createInvitationCommandHandler.handleAsync(command)

        Log.info("Sending invitation email with verificationCode: ${invitation.verificationCode.dropLast(3)}***")
        emailService.sendEmail(
            EmailTemplates.invitationEmail(
                email = invitation.email,
                verificationCode = invitation.verificationCode
            )
        )
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
        Log.info("User with role: ${createdUser.role} registered with id: ${createdUser.id}")
        if (createdUser.role == Role.PATIENT) {
            createdUser.groupId?.let { groupId ->
                assignPatientToGroupCommandHandler.handleAsync(
                    AssignPatientToGroupCommand(
                        groupId = groupId,
                        patientId = createdUser.id
                    )
                )
            }
        }
    }
}