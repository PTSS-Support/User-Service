package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_EMAIL
import org.ptss.support.domain.constants.ValidationMessages.INVALID_EMAIL

data class UserInvitationRequest(
    @field:NotBlank(message = EMPTY_EMAIL)
    @field:Email(message = INVALID_EMAIL)
    val email: String
)