package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
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
import org.ptss.support.domain.interfaces.facades.IGroupFacade
import org.ptss.support.domain.interfaces.commands.groups.ICreateGroupCommandHandler
import org.ptss.support.domain.interfaces.commands.invitations.ICreateInvitationCommandHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetAllGroupsQueryHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupMembersQueryHandler
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupUsersQueryHandler
import org.ptss.support.domain.interfaces.queries.invitations.IGetPendingInvitationsByGroupQueryHandler
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery
import org.ptss.support.domain.queries.invitations.GetPendingInvitationsByGroupQuery
import java.util.UUID

@ApplicationScoped
class GroupFacade(
    private val createGroupCommandHandler: ICreateGroupCommandHandler,
    private val createInvitationCommandHandler: ICreateInvitationCommandHandler,
    private val getAllGroupsQueryHandler: IGetAllGroupsQueryHandler,
    private val getGroupMembersQueryHandler: IGetGroupMembersQueryHandler,
    private val getGroupUsersQueryHandler: IGetGroupUsersQueryHandler,
    private val getPendingGroupInvitationsByGroupQueryHandler: IGetPendingInvitationsByGroupQueryHandler
) : IGroupFacade {

    override suspend fun getAllGroups(limit: Int?, cursor: UUID?): CursorPage<GroupResponse> {
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
        createInvitationCommandHandler.handleAsync(createInvitationCommand)

        return group.toResponse()
    }

    override suspend fun getGroupMembers(): List<UserResponse> {
        val query = GetGroupMembersQuery(
            // Note: The specific group ID should come from the current user's context
            // This would typically be injected via a security context or similar
            groupId = UUID.randomUUID() // TODO: Replace with actual group ID from context
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
        val query = GetPendingInvitationsByGroupQuery(
            // Note: The specific group ID should come from the current user's context
            // This would typically be injected via a security context or similar
            groupId = UUID.randomUUID() // TODO: Replace with actual group ID from context
        )
        return getPendingGroupInvitationsByGroupQueryHandler.handleAsync(query)
            .toInvitationResponse()
    }
}