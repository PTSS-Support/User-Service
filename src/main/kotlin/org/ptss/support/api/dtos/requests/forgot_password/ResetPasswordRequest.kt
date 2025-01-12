package org.ptss.support.api.dtos.requests.forgot_password

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.ptss.support.domain.constants.ValidationConstraints.PASSWORD_PATTERN
import org.ptss.support.domain.constants.ValidationConstraints.PASSWORD_RESET_CODE_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_RESET_PASSWORD_VERIFICATION_CODE
import org.ptss.support.domain.constants.ValidationMessages.INVALID_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.INVALID_RESET_PASSWORD_VERIFICATION_CODE

data class ResetPasswordRequest(
    @field:NotBlank(message = EMPTY_RESET_PASSWORD_VERIFICATION_CODE)
    @field:Pattern(
        regexp = PASSWORD_RESET_CODE_PATTERN,
        message = INVALID_RESET_PASSWORD_VERIFICATION_CODE
    )
    val resetCode: String,

    @field:NotBlank(message = EMPTY_PASSWORD)
    @field:Pattern(
        regexp = PASSWORD_PATTERN,
        message = INVALID_PASSWORD
    )
    val newPassword: String
)