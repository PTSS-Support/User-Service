package org.ptss.support.infrastructure.external_services.auth.clients

import jakarta.ws.rs.core.NewCookie
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*
import org.ptss.support.infrastructure.external_services.auth.dtos.responses.AuthIdentityResponse

interface IAuthenticationServiceClient {
    suspend fun login(request: AuthLoginRequest): Array<NewCookie>
    suspend fun loginWithPin(request: AuthPinLoginRequest): Array<NewCookie>
    suspend fun logout()

    // Identity management
    suspend fun createIdentity(request: AuthCreateIdentityRequest): AuthIdentityResponse
    suspend fun deleteIdentity(id: String)
    suspend fun updateRole(id: String, request: AuthUpdateRoleRequest): AuthIdentityResponse
    suspend fun updatePassword(id: String, request: AuthUpdatePasswordRequest)
    suspend fun createPin(id: String, request: AuthCreatePinRequest)
    suspend fun updatePin(id: String, request: AuthUpdatePinRequest)

    suspend fun getIdentityByEmail(email: String): AuthIdentityResponse {
        // TODO: Implement when Authentication Service adds this endpoint
        throw NotImplementedError("getIdentityByEmail endpoint not yet implemented in Authentication Service")
    }

    suspend fun resetPassword(request: AuthResetPasswordRequest) {
        // TODO: Implement when Authentication Service adds this endpoint
        throw NotImplementedError("resetPassword endpoint not yet implemented in Authentication Service")
    }
}