package org.ptss.support.api.dtos.requests.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.ptss.support.domain.constants.ValidationConstraints.PIN_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_CURRENT_PIN_CODE
import org.ptss.support.domain.constants.ValidationMessages.INVALID_CURRENT_PIN_CODE
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_NEW_PIN_CODE
import org.ptss.support.domain.constants.ValidationMessages.INVALID_NEW_PIN_CODE

data class PinUpdateRequest(
    @field:NotBlank(message = EMPTY_CURRENT_PIN_CODE)
    @field:Pattern(
        regexp = PIN_PATTERN,
        message = INVALID_CURRENT_PIN_CODE
    )
    val currentPin: String,
    @field:NotBlank(message = EMPTY_NEW_PIN_CODE)
    @field:Pattern(
        regexp = PIN_PATTERN,
        message = INVALID_NEW_PIN_CODE
    )
    val newPin: String
)