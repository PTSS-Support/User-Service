package org.ptss.support.domain.interfaces.controllers

import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.forgot_password.PasswordResetVerificationRequest
import org.ptss.support.api.dtos.requests.forgot_password.RequestPasswordResetRequest
import org.ptss.support.api.dtos.requests.forgot_password.ResetPasswordRequest

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IForgotPasswordController {
    @POST
    @Path("/forgot-password")
    @Operation(summary = "Request password reset code")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Password reset code sent successfully"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized access"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun requestPasswordReset(@Valid request: RequestPasswordResetRequest): Response

    @POST
    @Path("/forgot-password/verify")
    @Operation(summary = "Verify password reset code")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Reset code verified successfully"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized access"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun verifyPasswordReset(@Valid request: PasswordResetVerificationRequest): Response

    @POST
    @Path("/forgot-password/reset")
    @Operation(summary = "Reset password")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Password reset successfully"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized access"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        )
    )
    suspend fun resetPassword(@Valid request: ResetPasswordRequest): Response
}
