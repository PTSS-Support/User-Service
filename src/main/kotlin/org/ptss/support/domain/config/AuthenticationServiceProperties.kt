package org.ptss.support.domain.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class AuthenticationServiceProperties @Inject constructor(
    @ConfigProperty(name = "AUTH_SERVICE_URL", defaultValue = "https://localhost:8081")
    val baseUrl: String,
)