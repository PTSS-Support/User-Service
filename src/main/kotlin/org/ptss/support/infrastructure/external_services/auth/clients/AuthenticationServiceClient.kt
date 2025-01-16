package org.ptss.support.infrastructure.external_services.auth.clients

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.NewCookie
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*
import org.ptss.support.infrastructure.external_services.auth.dtos.responses.IdentityResponse
import org.ptss.support.infrastructure.external_services.clients.BaseClient

@ApplicationScoped
class AuthenticationServiceClient @Inject constructor(
    @ConfigProperty(name = "auth.service.url") baseUrl: String
) : BaseClient(baseUrl), IAuthenticationServiceClient {

    private val client: AuthenticationServiceApi by lazy { getClient() }

    override suspend fun login(request: LoginRequest): Array<NewCookie> =
        executeRequest(Array<NewCookie>::class.java) { client.login(request) }

    override suspend fun loginWithPin(request: PinLoginRequest): Array<NewCookie> =
        executeRequest(Array<NewCookie>::class.java) { client.loginWithPin(request) }

    override suspend fun logout() {
        executeRequest(Unit::class.java) { client.logout() }
    }

    override suspend fun createIdentity(request: CreateIdentityRequest): IdentityResponse =
        executeRequest(IdentityResponse::class.java) { client.createIdentity(request) }

    override suspend fun deleteIdentity(id: String) {
        executeRequest(Unit::class.java) { client.deleteIdentity(id) }
    }

    override suspend fun updateRole(id: String, request: UpdateRoleRequest): IdentityResponse =
        executeRequest(IdentityResponse::class.java) { client.updateRole(id, request) }

    override suspend fun updatePassword(id: String, request: UpdatePasswordRequest) {
        executeRequest(Unit::class.java) { client.updatePassword(id, request) }
    }

    override suspend fun createPin(id: String, request: CreatePinRequest) {
        executeRequest(Unit::class.java) { client.createPin(id, request) }
    }

    override suspend fun updatePin(id: String, request: UpdatePinRequest) {
        executeRequest(Unit::class.java) { client.updatePin(id, request) }
    }
}