package org.ptss.support.infrastructure.external_services.auth.dtos.requests

data class AuthLoginRequest(
    val email: String,
    val password: String
)