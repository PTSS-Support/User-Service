package org.ptss.support.domain.interfaces.facades

import org.ptss.support.api.dtos.requests.groups.AssignPrimaryCaregiverRequest
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import java.util.UUID

interface IGroupFacade {
    suspend fun getAllGroups(limit: Int = DEFAULT_LIMIT, cursor: UUID?): CursorPage<GroupResponse>

    suspend fun createGroup(request: CreateGroupRequest): GroupResponse

    suspend fun getGroupMembers(): List<UserResponse>

    suspend fun getGroupUsers(groupId: UUID): List<UserResponse>

    suspend fun getPendingGroupInvitations(): List<InvitationResponse>

    suspend fun assignPrimaryCaregiver(request: AssignPrimaryCaregiverRequest): GroupResponse

    suspend fun removePrimaryCaregiver()
}