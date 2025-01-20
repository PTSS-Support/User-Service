package org.ptss.support.infrastructure.external_services.auth.clients

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.NewCookie
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.CircuitBreaker
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*
import org.ptss.support.infrastructure.external_services.auth.dtos.responses.AuthIdentityResponse
import org.ptss.support.infrastructure.external_services.clients.BaseClient

@ApplicationScoped
class AuthenticationServiceClient(
    @ConfigProperty(name = "auth.service.url") baseUrl: String
) : BaseClient(baseUrl), IAuthenticationServiceClient {
    // No-args constructor for CDI
    constructor() : this("")

    private val client: AuthenticationServiceApi by lazy { getClient() }

    override suspend fun login(request: AuthLoginRequest): Array<NewCookie> =
        executeRequest(Array<NewCookie>::class.java) { client.login(request) }

    override suspend fun logout() {
        executeRequest(Unit::class.java) { client.logout() }
    }

    @CircuitBreaker
    @Bulkhead
    @Fallback(fallbackMethod = "createIdentityFallback")
    override suspend fun createIdentity(request: AuthCreateIdentityRequest): AuthIdentityResponse =
        executeRequest(AuthIdentityResponse::class.java) { client.createIdentity(request) }

    private suspend fun createIdentityFallback(request: AuthCreateIdentityRequest): AuthIdentityResponse {
        throw CircuitBreakerOpenException("Identity creation temporarily unavailable")
    }

    @CircuitBreaker
    @Bulkhead
    @Fallback(fallbackMethod = "deleteIdentityFallback")
    override suspend fun deleteIdentity(id: String) {
        executeRequest(Unit::class.java) { client.deleteIdentity(id) }
    }

    private suspend fun deleteIdentityFallback(id: String) {
        throw CircuitBreakerOpenException("Identity deletion temporarily unavailable")
    }

    @CircuitBreaker
    @Bulkhead
    @Fallback(fallbackMethod = "updateRoleFallback")
    override suspend fun updateRole(id: String, request: AuthUpdateRoleRequest): AuthIdentityResponse =
        executeRequest(AuthIdentityResponse::class.java) { client.updateRole(id, request) }

    private suspend fun updateRoleFallback(id: String, request: AuthUpdateRoleRequest): AuthIdentityResponse {
        throw CircuitBreakerOpenException("Role update temporarily unavailable")
    }

    override suspend fun updatePassword(id: String, request: AuthUpdatePasswordRequest) {
        executeRequest(Unit::class.java) { client.updatePassword(id, request) }
    }

    override suspend fun createPin(id: String, request: AuthCreatePinRequest) {
        executeRequest(Unit::class.java) { client.createPin(id, request) }
    }

    override suspend fun updatePin(id: String, request: AuthUpdatePinRequest) {
        executeRequest(Unit::class.java) { client.updatePin(id, request) }
    }
}