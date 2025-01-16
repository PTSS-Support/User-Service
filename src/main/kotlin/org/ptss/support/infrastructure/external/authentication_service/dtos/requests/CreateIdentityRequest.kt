package org.ptss.support.infrastructure.external.authentication_service.dtos.requests

import org.ptss.support.domain.enums.Role

data class CreateIdentityRequest(
    val email: String,
    val password: String,
    val role: Role,
    val groupId: String?,
    val firstName: String,
    val lastName: String
)