package org.ptss.support.infrastructure.external_services.auth.dtos.requests

data class AuthUpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)