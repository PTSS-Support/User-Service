package org.ptss.support.infrastructure.external_services.auth.clients

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface AuthenticationServiceApi {
    @POST
    @Path("/login")
    fun login(request: LoginRequest): Response

    @POST
    @Path("/login/pin")
    fun loginWithPin(request: PinLoginRequest): Response

    @POST
    @Path("/logout")
    fun logout(): Response

    @POST
    @Path("/identity")
    fun createIdentity(request: CreateIdentityRequest): Response

    @DELETE
    @Path("/identity/{id}")
    fun deleteIdentity(@PathParam("id") id: String): Response

    @PATCH
    @Path("/identity/{id}/role")
    fun updateRole(@PathParam("id") id: String, request: UpdateRoleRequest): Response

    @PATCH
    @Path("/identity/{id}/password")
    fun updatePassword(@PathParam("id") id: String, request: UpdatePasswordRequest): Response

    @POST
    @Path("/identity/{id}/pin")
    fun createPin(@PathParam("id") id: String, request: CreatePinRequest): Response

    @PATCH
    @Path("/identity/{id}/pin")
    fun updatePin(@PathParam("id") id: String, request: UpdatePinRequest): Response
}