package org.ptss.support.api.dtos.requests.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.ptss.support.domain.constants.ValidationConstraints.PASSWORD_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.INVALID_PASSWORD

data class PasswordUpdateRequest(
    @field:NotBlank(message = EMPTY_PASSWORD)
    @field:Pattern(
        regexp = PASSWORD_PATTERN,
        message = INVALID_PASSWORD
    )
    val currentPassword: String,
    @field:NotBlank(message = EMPTY_PASSWORD)
    @field:Pattern(
        regexp = PASSWORD_PATTERN,
        message = INVALID_PASSWORD
    )
    val newPassword: String
)