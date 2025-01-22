package org.ptss.support.infrastructure.external_services.auth.dtos.requests

import org.ptss.support.domain.enums.Role

data class AuthCreateIdentityRequest(
    val userId: String,
    val email: String,
    val password: String,
    val role: Role,
    val groupId: String?,
    val firstName: String,
    val lastName: String
)