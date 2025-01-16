package org.ptss.support.infrastructure.external.authentication_service.dtos.responses

import org.ptss.support.domain.enums.Role

data class IdentityResponse(
    val id: String,
    val email: String,
    val role: Role
)