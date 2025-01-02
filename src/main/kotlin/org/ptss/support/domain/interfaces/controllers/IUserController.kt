package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.common.pagination.CursorPage
import java.util.*

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IUserController {
    @GET
    @Operation(summary = "Get all users")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of users with pagination metadata",
            content = [Content(schema = Schema(implementation = CursorPage::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun getAllUsers(
        @QueryParam("limit") limit: Int?,
        @QueryParam("cursor") cursor: UUID?
    ): CursorPage<UserResponse>

    @GET
    @Path("/me")
    @Operation(summary = "Get current user profile")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Current user profile",
            content = [Content(schema = Schema(implementation = UserResponse::class))]
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized"
        )
    )
    suspend fun getCurrentUser(): UserResponse

    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "User details",
            content = [Content(schema = Schema(implementation = UserResponse::class))]
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        ),
        APIResponse(
            responseCode = "404",
            description = "Not found"
        )
    )
    suspend fun getUserById(
        @Parameter(description = "ID of the user", required = true)
        @PathParam("id") id: UUID
    ): UserResponse

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete user")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "User deleted successfully"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        ),
        APIResponse(
            responseCode = "404",
            description = "Not found"
        )
    )
    suspend fun deleteUser(
        @Parameter(description = "ID of the user", required = true)
        @PathParam("id") id: UUID
    ): Response
}