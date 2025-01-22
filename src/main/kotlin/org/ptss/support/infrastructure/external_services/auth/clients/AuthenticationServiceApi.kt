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
    suspend fun login(request: AuthLoginRequest): Response

    @POST
    @Path("/logout")
    suspend fun logout(): Response

    @POST
    @Path("/identity")
    suspend fun createIdentity(request: AuthCreateIdentityRequest): Response

    @DELETE
    @Path("/identity/{id}")
    suspend fun deleteIdentity(@PathParam("id") id: String): Response

    @PATCH
    @Path("/identity/{id}/role")
    suspend fun updateRole(@PathParam("id") id: String, request: AuthUpdateRoleRequest): Response

    @PATCH
    @Path("/identity/{id}/password")
    suspend fun updatePassword(@PathParam("id") id: String, request: AuthUpdatePasswordRequest): Response

    @POST
    @Path("/identity/{id}/pin")
    suspend fun createPin(@PathParam("id") id: String, request: AuthCreatePinRequest): Response

    @PATCH
    @Path("/identity/{id}/pin")
    suspend fun updatePin(@PathParam("id") id: String, request: AuthUpdatePinRequest): Response
}