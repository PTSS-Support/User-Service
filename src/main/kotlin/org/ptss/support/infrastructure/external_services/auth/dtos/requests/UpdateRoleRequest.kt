package org.ptss.support.infrastructure.external_services.auth.dtos.requests

import org.ptss.support.domain.enums.Role

data class UpdateRoleRequest(
    val role: Role
)