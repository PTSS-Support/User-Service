package org.ptss.support.domain.interfaces.facades

import jakarta.ws.rs.core.NewCookie
import org.ptss.support.api.dtos.requests.auth.*

interface IAuthFacade {
    suspend fun login(request: LoginRequest): Array<NewCookie>

    suspend fun loginWithPin(request: PinLoginRequest): Array<NewCookie>

    suspend fun logout()

    suspend fun createPin(request: PinCreateRequest)

    suspend fun updatePin(request: PinUpdateRequest)

    suspend fun updatePassword(request: PasswordUpdateRequest)
}