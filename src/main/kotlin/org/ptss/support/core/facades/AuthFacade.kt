package org.ptss.support.core.facades

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.NewCookie
import org.ptss.support.api.dtos.requests.auth.*
import org.ptss.support.domain.interfaces.facades.IAuthFacade
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.toExternalDto
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.ptss.support.security.context.AuthenticatedUserContext

@ApplicationScoped
class AuthFacade @Inject constructor(
    private val authenticationServiceClient: IAuthenticationServiceClient,
    private val userContext: AuthenticatedUserContext
) : IAuthFacade {

    override suspend fun login(request: LoginRequest): Array<NewCookie> {
        return executeWithExceptionLoggingAsync(
            operation = {
                authenticationServiceClient.login(request.toExternalDto())
            },
            logMessage = "Failed to login user with email: ${request.email}"
        )
    }

    override suspend fun loginWithPin(request: PinLoginRequest): Array<NewCookie> {
        return executeWithExceptionLoggingAsync(
            operation = {
                authenticationServiceClient.loginWithPin(request.toExternalDto())
            },
            logMessage = "Failed to login user with PIN"
        )
    }

    override suspend fun logout() {
        executeWithExceptionLoggingAsync(
            operation = {
                try {
                    authenticationServiceClient.logout()
                } catch (ex: Exception) {
                    // Silently catch all exceptions as per requirements
                    Log.warn("Failed to logout user, but continuing as per requirements", ex)
                }
            },
            logMessage = "Attempted to log out user"
        )
    }

    override suspend fun createPin(request: PinCreateRequest) {
        executeWithExceptionLoggingAsync(
            operation = {
                val user = userContext.getCurrentUser()
                if (user.hasPin) {
                    throw IllegalStateException("PIN already exists for this user")
                }
                authenticationServiceClient.createPin(
                    id = user.userId.toString(),
                    request = request.toExternalDto()
                )
            },
            logMessage = "Failed to create PIN"
        )
    }

    override suspend fun updatePin(request: PinUpdateRequest) {
        executeWithExceptionLoggingAsync(
            operation = {
                val user = userContext.getCurrentUser()
                if (!user.hasPin) {
                    throw IllegalStateException("Cannot update PIN: No PIN is set for this user")
                }
                authenticationServiceClient.updatePin(
                    id = user.userId.toString(),
                    request = request.toExternalDto()
                )
            },
            logMessage = "Failed to update PIN"
        )
    }

    override suspend fun updatePassword(request: PasswordUpdateRequest) {
        executeWithExceptionLoggingAsync(
            operation = {
                val user = userContext.getCurrentUser()
                authenticationServiceClient.updatePassword(
                    id = user.userId.toString(),
                    request = request.toExternalDto()
                )
            },
            logMessage = "Failed to update password"
        )
    }
}