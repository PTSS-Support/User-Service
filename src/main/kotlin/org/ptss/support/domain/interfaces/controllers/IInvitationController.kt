package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.invitations.CreateInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.Authentication

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IInvitationController {
    @POST
    @Path("/invite")
    @Operation(summary = "Invite user to join group")
    @Authentication(roles = [Role.PATIENT, Role.PRIMARY_CAREGIVER])
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Invitation created & sent successfully"
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
    suspend fun inviteUser(request: CreateInvitationRequest): Response

    @POST
    @Path("/invite/verify")
    @Operation(summary = "Verify invitation code")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Invitation code verified successfully"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "404",
            description = "Invitation not found or expired"
        )
    )
    suspend fun verifyInvitation(request: UserInvitationVerificationRequest): Response

    @POST
    @Path("/register")
    @Operation(summary = "Complete user registration")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "User registered successfully"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        )
    )
    suspend fun registerUser(request: UserRegistrationRequest): Response
}
