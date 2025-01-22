package org.ptss.support.api.dtos.requests.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.PASSWORD_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMAIL_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.INVALID_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.INVALID_EMAIL
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_EMAIL

data class LoginRequest(
    @field:NotBlank(message = EMPTY_EMAIL)
    @field:Email(message = INVALID_EMAIL)
    @field:Size(
        max = EMAIL_MAX_LENGTH,
        message = EMAIL_LENGTH
    )
    val email: String,
    @field:NotBlank(message = EMPTY_PASSWORD)
    @field:Pattern(
        regexp = PASSWORD_PATTERN,
        message = INVALID_PASSWORD
    )
    val password: String
)