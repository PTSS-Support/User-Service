package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.requests.toCreateGroupCommand
import org.ptss.support.api.dtos.requests.toCreateInvitationCommand
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.commands.groups.CreateGroupCommand
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.interfaces.facades.IGroupFacade
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery
import java.util.UUID

@ApplicationScoped
class GroupFacade(
    private val createGroupCommandHandler: ICommandHandler<CreateGroupCommand, GroupResponse>,
    private val createInvitationCommandHandler: ICommandHandler<CreateInvitationCommand, Unit>,
    private val getAllGroupsQueryHandler: IQueryHandler<GetAllGroupsQuery, CursorPage<GroupResponse>>,
    private val getGroupMembersQueryHandler: IQueryHandler<GetGroupMembersQuery, List<UserResponse>>,
    private val getGroupUsersQueryHandler: IQueryHandler<GetGroupUsersQuery, List<UserResponse>>,
    private val getPendingGroupInvitationsQueryHandler: IQueryHandler<GetGroupUsersQuery, List<InvitationResponse>>
) : IGroupFacade {

    override suspend fun getAllGroups(limit: Int?, cursor: UUID?): CursorPage<GroupResponse> {
        val query = GetAllGroupsQuery()
        return getAllGroupsQueryHandler.handleAsync(query)
    }

    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        // First, create the group
        val createGroupCommand = request.toCreateGroupCommand()
        val groupResponse = createGroupCommandHandler.handleAsync(createGroupCommand)

        // Then, create an invitation for the patient
        val createInvitationCommand = request.toCreateInvitationCommand(groupResponse.id)
        createInvitationCommandHandler.handleAsync(createInvitationCommand)

        return groupResponse
    }

    override suspend fun getGroupMembers(): List<UserResponse> {
        val query = GetGroupMembersQuery(
            // Note: The specific group ID should come from the current user's context
            // This would typically be injected via a security context or similar
            groupId = UUID.randomUUID() // TODO: Replace with actual group ID from context
        )
        return getGroupMembersQueryHandler.handleAsync(query)
    }

    override suspend fun getGroupUsers(groupId: UUID): List<UserResponse> {
        val query = GetGroupUsersQuery(groupId = groupId)
        return getGroupUsersQueryHandler.handleAsync(query)
    }

    override suspend fun getPendingGroupInvitations(): List<InvitationResponse> {
        val query = GetGroupUsersQuery(
            // Note: The specific group ID should come from the current user's context
            // This would typically be injected via a security context or similar
            groupId = UUID.randomUUID() // TODO: Replace with actual group ID from context
        )
        return getPendingGroupInvitationsQueryHandler.handleAsync(query)
    }
}