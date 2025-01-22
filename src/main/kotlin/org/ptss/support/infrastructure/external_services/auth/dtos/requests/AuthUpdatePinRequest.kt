package org.ptss.support.infrastructure.external_services.auth.dtos.requests

data class AuthUpdatePinRequest(
    val oldPin: String,
    val newPin: String
)