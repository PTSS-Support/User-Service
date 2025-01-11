package org.ptss.support.api.dtos.requests.forgot_password

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.EMAIL_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_EMAIL
import org.ptss.support.domain.constants.ValidationMessages.INVALID_EMAIL

data class RequestPasswordResetRequest(
    @field:NotBlank(message = EMPTY_EMAIL)
    @field:Email(message = INVALID_EMAIL)
    @field:Size(
        max = EMAIL_MAX_LENGTH,
        message = EMAIL_LENGTH
    )
    val email: String
)