package org.ptss.support.infrastructure.external.authentication_service.dtos.requests

data class UpdatePinRequest(
    val oldPin: String,
    val newPin: String
)