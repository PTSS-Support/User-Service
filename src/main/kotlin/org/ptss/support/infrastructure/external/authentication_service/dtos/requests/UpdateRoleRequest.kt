package org.ptss.support.infrastructure.external.authentication_service.dtos.requests

import org.ptss.support.domain.enums.Role

data class UpdateRoleRequest(
    val role: Role
)