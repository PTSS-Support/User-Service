package org.ptss.support.infrastructure.external_services.auth.dtos.requests

class AuthResetPasswordRequest(
    val email: String,
    val newPassword: String
)