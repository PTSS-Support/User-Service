package org.ptss.support.infrastructure.external.authentication_service.dtos.requests

data class UpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)