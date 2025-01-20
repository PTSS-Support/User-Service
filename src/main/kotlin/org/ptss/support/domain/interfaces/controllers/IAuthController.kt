package org.ptss.support.domain.interfaces.controllers

import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.auth.*

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IAuthController {
    @POST
    @Path("/login")
    @Operation(summary = "Login with email and password")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Login successful",
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized"
        )
    )
    suspend fun login(@Valid request: LoginRequest): Response

    @POST
    @Path("/me/logout")
    @Operation(summary = "User logout", description = "Invalidates the user's session in Keycloak")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Logout successful"
        )
    )
    suspend fun logout(): Response

    @POST
    @Path("/me/pin")
    @Operation(summary = "Create initial PIN", description = "Set PIN for the first time when no PIN exists")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "PIN created successfully"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        ),
        APIResponse(
            responseCode = "409",
            description = "PIN already exists"
        )
    )
    suspend fun createPin(@Valid request: PinCreateRequest): Response

    @PUT
    @Path("/me/pin")
    @Operation(summary = "Update your PIN")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "PIN updated successfully"
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
    suspend fun updatePin(@Valid request: PinUpdateRequest): Response

    @PUT
    @Path("/me/password")
    @Operation(summary = "Update your password")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Password updated successfully"
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
    suspend fun updatePassword(@Valid request: PasswordUpdateRequest): Response
}