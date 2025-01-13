package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.NewCookie
import org.ptss.support.api.dtos.requests.auth.*
import org.ptss.support.domain.interfaces.facades.IAuthFacade
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import io.quarkus.logging.Log

@ApplicationScoped
class AuthFacade @Inject constructor(
    // TODO: Inject AuthenticationServiceClient once it's created
) : IAuthFacade {

    override suspend fun login(request: LoginRequest): Array<NewCookie> {
        return executeWithExceptionLoggingAsync(
            operation = {
                // TODO: Make a call to the authentication service for login
                // The authentication service should handle the validation and return the appropriate cookies
                // Example structure of what needs to be implemented:
                // val response = authenticationServiceClient.login(request)
                // return response.cookies
                emptyArray()
            },
            logMessage = "Failed to login user with email: ${request.email}"
        )
    }

    override suspend fun loginWithPin(request: PinLoginRequest): Array<NewCookie> {
        return executeWithExceptionLoggingAsync(
            operation = {
                // TODO: Make a call to the authentication service for PIN login
                // The authentication service should validate the PIN and refresh token from cookies
                // and return new cookies if successful
                // Example structure:
                // val response = authenticationServiceClient.loginWithPin(request)
                // return response.cookies
                emptyArray()
            },
            logMessage = "Failed to login user with PIN"
        )
    }

    override suspend fun logout() {
        executeWithExceptionLoggingAsync(
            operation = {
                try {
                    // TODO: Make a call to the authentication service to invalidate the session
                    // Example:
                    // authenticationServiceClient.logout()
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
                // TODO: Make a call to the authentication service to create the PIN
                // Example:
                // authenticationServiceClient.createPin(request)
            },
            logMessage = "Failed to create PIN"
        )
    }

    override suspend fun updatePin(request: PinUpdateRequest) {
        executeWithExceptionLoggingAsync(
            operation = {
                // TODO: Make a call to the authentication service to update the PIN
                // Example:
                // authenticationServiceClient.updatePin(request)
            },
            logMessage = "Failed to update PIN"
        )
    }

    override suspend fun updatePassword(request: PasswordUpdateRequest) {
        executeWithExceptionLoggingAsync(
            operation = {
                // TODO: Make a call to the authentication service to update the password
                // Example:
                // authenticationServiceClient.updatePassword(request)
            },
            logMessage = "Failed to update password"
        )
    }
}