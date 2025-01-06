package org.ptss.support.security.context

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.domain.constants.SecurityMessages.UNAUTHORIZED_ACCESS

@ApplicationScoped
class AuthenticatedUserContext @Inject constructor() {
    private val userContext = ThreadLocal<UserContext>()

    fun getCurrentUser(): UserContext = userContext.get()
        ?: throw UnauthorizedException(UNAUTHORIZED_ACCESS)

    internal fun setCurrentUser(context: UserContext) {
        userContext.set(context)
    }

    internal fun clearCurrentUser() {
        userContext.remove()
    }
}