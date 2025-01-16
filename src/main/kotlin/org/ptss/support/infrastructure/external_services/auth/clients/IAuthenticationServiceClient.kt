package org.ptss.support.infrastructure.external_services.auth.clients

import jakarta.ws.rs.core.NewCookie
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*
import org.ptss.support.infrastructure.external_services.auth.dtos.responses.IdentityResponse

interface IAuthenticationServiceClient {
    suspend fun login(request: LoginRequest): Array<NewCookie>
    suspend fun loginWithPin(request: PinLoginRequest): Array<NewCookie>
    suspend fun logout()

    // Identity management
    suspend fun createIdentity(request: CreateIdentityRequest): IdentityResponse
    suspend fun deleteIdentity(id: String)
    suspend fun updateRole(id: String, request: UpdateRoleRequest): IdentityResponse
    suspend fun updatePassword(id: String, request: UpdatePasswordRequest)
    suspend fun createPin(id: String, request: CreatePinRequest)
    suspend fun updatePin(id: String, request: UpdatePinRequest)
}