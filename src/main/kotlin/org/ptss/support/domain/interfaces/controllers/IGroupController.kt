package org.ptss.support.domain.interfaces.controllers

import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.constants.PaginationConstants.DEFAULT_LIMIT
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.Authentication
import java.util.UUID

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IGroupController {
    @GET
    @Operation(summary = "Get all groups", description = "Retrieves a list of all groups")
    @Authentication(roles = [Role.ADMIN, Role.HCP])
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of groups with pagination metadata",
            content = [Content(schema = Schema(implementation = CursorPage::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun getAllGroups(
        @QueryParam("limit") limit: Int = DEFAULT_LIMIT,
        @QueryParam("cursor") cursor: UUID?
    ): CursorPage<GroupResponse>

    @POST
    @Operation(summary = "Create new group", description = "Creates a new group")
    @Authentication(roles = [Role.ADMIN, Role.HCP])
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Group created successfully",
            content = [Content(schema = Schema(implementation = GroupResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun createGroup(@Valid request: CreateGroupRequest): GroupResponse

    @GET
    @Path("/members")
    @Operation(summary = "Get all members of your group", description = "Retrieves all members of the current user's group")
    @Authentication(roles = [Role.ADMIN, Role.HCP, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of group members successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<UserResponse>::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun getGroupMembers(): List<UserResponse>

    @GET
    @Path("/{id}/users")
    @Operation(summary = "Get all users of a group", description = "Retrieves all users of a specific group")
    @Authentication(roles = [Role.ADMIN])
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of group users successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<UserResponse>::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun getGroupUsers(
        @Parameter(description = "ID of the group", required = true)
        @PathParam("id") id: UUID
    ): List<UserResponse>

    @GET
    @Path("/invitations/pending")
    @Operation(summary = "Get all pending invitations of your group", description = "Retrieves all pending invitations of your specific group")
    @Authentication(roles = [Role.ADMIN, Role.HCP, Role.PATIENT, Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER])
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of pending group invitations successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<InvitationResponse>::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun getPendingGroupInvitations(): List<InvitationResponse>
}