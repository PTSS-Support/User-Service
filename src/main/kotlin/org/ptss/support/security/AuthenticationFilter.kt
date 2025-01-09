package org.ptss.support.security

import io.quarkus.logging.Log
import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.ext.Provider
import org.ptss.support.domain.config.SecurityProperties
import org.ptss.support.domain.constants.SecurityMessages.MISSING_GROUP
import org.ptss.support.domain.constants.SecurityMessages.UNAUTHORIZED_ACCESS
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.context.AuthenticatedUserContext
import org.ptss.support.security.context.UserContext

@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val tokenUserExtractor: TokenUserExtractor,
    private val securityProperties: SecurityProperties,
    private val userContext: AuthenticatedUserContext
) : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return

        val accessToken = runCatching {
            getAccessToken(requestContext)
        }.getOrNull() ?: run {
            Log.error("Failed to get access token")
            throw UnauthorizedException(UNAUTHORIZED_ACCESS)
        }

        val context = tokenUserExtractor.extractUserContext(accessToken, securityProperties.keycloakPublicKey, securityProperties.jwtValidationEnabled)
            .getOrElse {
                Log.error("Failed to extract user context: ${it.message}")
                throw UnauthorizedException(UNAUTHORIZED_ACCESS)
            }

        Log.info("Authenticated user ${context.userId} with roles: ${context.roles}")
        if (context.groupId != null) {
            Log.info("User belongs to group: ${context.groupId}")
        }
        Log.debug("Full authentication context: $context")

        // Check if user has any valid roles at all
        if (context.roles.isEmpty()) {
            Log.error("Failed to extract roles or token expired")
            throw UnauthorizedException(UNAUTHORIZED_ACCESS)
        }

        // Validate roles against annotation requirements
        val requiredRoles = annotation.roles.toSet()
        if (requiredRoles.isNotEmpty() && context.roles.none { it in requiredRoles }) {
            Log.warn("User does not have required roles for request")
            throw UnauthorizedException(UNAUTHORIZED_ACCESS)
        }

        validateGroupIdConstraints(context)

        userContext.setCurrentUser(context)
    }

    // Just a little extra line of defense
    // See Defense in depth: https://en.wikipedia.org/wiki/Defense_in_depth_(computing)
    private fun validateGroupIdConstraints(context: UserContext) {
        val isAdminOrHCP = context.roles.any { it == Role.ADMIN || it == Role.HCP }
        if (!isAdminOrHCP && context.groupId == null) {
            Log.error("User with roles \"${context.roles}\" does not have a group id")
            throw UnauthorizedException(MISSING_GROUP)
        }
    }

    private fun getAccessToken(requestContext: ContainerRequestContext): String {
        val token = requestContext.cookies[securityProperties.accessTokenCookieName]?.value
        Log.debug("Token present: ${token != null}")

        if (token.isNullOrBlank()) {
            Log.error("Token not found in cookie")
            throw UnauthorizedException(UNAUTHORIZED_ACCESS)
        }

        return token
    }

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod?.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass?.getAnnotation(Authentication::class.java)
}

