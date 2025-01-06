package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IGroupController
import org.ptss.support.domain.interfaces.facades.IGroupFacade
import org.ptss.support.security.Authentication
import java.util.UUID

@ApplicationScoped
class GroupController @Inject constructor(
    private val groupFacade: IGroupFacade
) : IGroupController {

    @Authentication(roles = [Role.ADMIN, Role.HCP])
    override suspend fun getAllGroups(limit: Int, cursor: UUID?): CursorPage<GroupResponse> =
       groupFacade.getAllGroups(limit, cursor)

    @Authentication(roles = [Role.ADMIN, Role.HCP])
    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        return groupFacade.createGroup(request)
    }

    @Authentication(roles = [Role.ADMIN, Role.HCP, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun getGroupMembers(): List<UserResponse> {
        return groupFacade.getGroupMembers()
    }

    @Authentication(roles = [Role.ADMIN])
    override suspend fun getGroupUsers(id: UUID): List<UserResponse> {
        return groupFacade.getGroupUsers(id)
    }

    @Authentication(roles = [Role.ADMIN, Role.HCP, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    override suspend fun getPendingGroupInvitations(): List<InvitationResponse> {
        return groupFacade.getPendingGroupInvitations()
    }
}