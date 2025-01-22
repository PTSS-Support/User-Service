package org.ptss.support.core.facades

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.BadRequestException
import org.ptss.support.api.dtos.requests.groups.AssignPrimaryCaregiverRequest
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.requests.toCommand
import org.ptss.support.api.dtos.requests.toCreateGroupCommand
import org.ptss.support.api.dtos.requests.toCreateInvitationCommand
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.toInvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.api.dtos.responses.toResponse
import org.ptss.support.api.dtos.responses.toUserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.common.pagination.mapItems
import org.ptss.support.domain.commands.groups.RemovePrimaryCaregiverFromGroupCommand
import org.ptss.support.domain.constants.SecurityMessages.MISSING_GROUP
import org.ptss.support.domain.interfaces.commands.groups.IAssignPrimaryCaregiverToGroupCommandHandler
import org.ptss.support.domain.interfaces.facades.IGroupFacade
import org.ptss.support.domain.interfaces.commands.groups.ICreateGroupCommandHandler
import org.ptss.support.domain.interfaces.commands.groups.IRemovePrimaryCaregiverFromGroupCommandHandler
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetAllGroupsQueryHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupMembersQueryHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupUsersQueryHandler
import org.ptss.support.domain.interfaces.queries.invitations.IGetPendingInvitationsByGroupQueryHandler
import org.ptss.support.domain.interfaces.services.IEmailService
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery
import org.ptss.support.domain.queries.invitations.GetPendingInvitationsByGroupQuery
import org.ptss.support.domain.templates.EmailTemplates
import org.ptss.support.security.context.AuthenticatedUserContext
import java.util.UUID

@ApplicationScoped
class GroupFacade(
    private val createGroupCommandHandler: ICreateGroupCommandHandler,
    private val createInvitationCommandHandler: ICreateInvitationCommandHandler,
    private val getAllGroupsQueryHandler: IGetAllGroupsQueryHandler,
    private val getGroupMembersQueryHandler: IGetGroupMembersQueryHandler,
    private val getGroupUsersQueryHandler: IGetGroupUsersQueryHandler,
    private val getPendingGroupInvitationsByGroupQueryHandler: IGetPendingInvitationsByGroupQueryHandler,
    private val assignPrimaryCaregiverCommandHandler: IAssignPrimaryCaregiverToGroupCommandHandler,
    private val removePrimaryCaregiverCommandHandler: IRemovePrimaryCaregiverFromGroupCommandHandler,
    private val emailService: IEmailService,
    private val userContext: AuthenticatedUserContext
) : IGroupFacade {

    override suspend fun getAllGroups(limit: Int, cursor: UUID?): CursorPage<GroupResponse> {
        val query = GetAllGroupsQuery(limit, cursor)
        return getAllGroupsQueryHandler.handleAsync(query)
            .mapItems { it.toResponse() }
    }

    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        // First, create the group
        val createGroupCommand = request.toCreateGroupCommand()
        val group = createGroupCommandHandler.handleAsync(createGroupCommand)

        // Then, create an invitation for the patient
        val createInvitationCommand = request.toCreateInvitationCommand(group.id)
        val invitation = createInvitationCommandHandler.handleAsync(createInvitationCommand)

        // Then, send an email
        Log.info("Sending invitation email with verificationCode: ${invitation.verificationCode.dropLast(3)}***")
        emailService.sendEmail(
            EmailTemplates.invitationEmail(
                email = invitation.email,
                verificationCode = invitation.verificationCode
            )
        )

        return group.toResponse()
    }

    override suspend fun getGroupMembers(): List<UserResponse> {
        val user = userContext.getCurrentUser()
        Log.info("Authenticated user $user with groupId ${user.groupId?: "ILLEGAL STATE"}")
        val query = GetGroupMembersQuery(
            groupId = user.groupId?: throw BadRequestException(MISSING_GROUP)
        )
        return getGroupMembersQueryHandler.handleAsync(query)
            .toUserResponse()
    }

    override suspend fun getGroupUsers(groupId: UUID): List<UserResponse> {
        val query = GetGroupUsersQuery(groupId = groupId)
        return getGroupUsersQueryHandler.handleAsync(query)
            .toUserResponse()
    }

    override suspend fun getPendingGroupInvitations(): List<InvitationResponse> {
        val user = userContext.getCurrentUser()
        Log.info("Authenticated user $user with groupId ${user.groupId?: "ILLEGAL STATE"}")
        val query = GetPendingInvitationsByGroupQuery(
            groupId = user.groupId?: throw BadRequestException(MISSING_GROUP)
        )
        return getPendingGroupInvitationsByGroupQueryHandler.handleAsync(query)
            .toInvitationResponse()
    }

    override suspend fun assignPrimaryCaregiver(request: AssignPrimaryCaregiverRequest): GroupResponse {
        val user = userContext.getCurrentUser()
        Log.info("Attempting to assign primary caregiver for group ${user.groupId}")

        val command = request.toCommand(user.groupId ?: throw BadRequestException(MISSING_GROUP))

        return assignPrimaryCaregiverCommandHandler.handleAsync(command).toResponse()
    }

    override suspend fun removePrimaryCaregiver() {
        val user = userContext.getCurrentUser()
        Log.info("Attempting to remove primary caregiver from group ${user.groupId}")

        val command = RemovePrimaryCaregiverFromGroupCommand(
            groupId = user.groupId ?: throw BadRequestException(MISSING_GROUP)
        )

        removePrimaryCaregiverCommandHandler.handleAsync(command)
    }
}