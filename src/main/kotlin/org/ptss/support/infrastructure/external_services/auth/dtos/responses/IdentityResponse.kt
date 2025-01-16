package org.ptss.support.infrastructure.external_services.auth.dtos.responses

import org.ptss.support.domain.enums.Role

data class IdentityResponse(
    val id: String,
    val email: String,
    val role: Role
)