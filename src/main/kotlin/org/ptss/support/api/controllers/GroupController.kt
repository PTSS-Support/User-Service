package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.controllers.IGroupController
import org.ptss.support.domain.interfaces.facades.IGroupFacade
import java.util.UUID

@ApplicationScoped
class GroupController @Inject constructor(
    private val groupFacade: IGroupFacade
) : IGroupController {

    override suspend fun getAllGroups(limit: Int?, cursor: UUID?): CursorPage<GroupResponse> {
        return groupFacade.getAllGroups(limit, cursor)
    }

    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        return groupFacade.createGroup(request)
    }

    override suspend fun getGroupMembers(): List<UserResponse> {
        return groupFacade.getGroupMembers()
    }

    override suspend fun getGroupUsers(id: UUID): List<UserResponse> {
        return groupFacade.getGroupUsers(id)
    }

    override suspend fun getPendingGroupInvitations(id: UUID): List<InvitationResponse> {
        return groupFacade.getPendingGroupInvitations(id)
    }
}