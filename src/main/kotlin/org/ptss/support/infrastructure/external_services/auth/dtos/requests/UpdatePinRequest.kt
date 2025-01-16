package org.ptss.support.infrastructure.external_services.auth.dtos.requests

data class UpdatePinRequest(
    val oldPin: String,
    val newPin: String
)