package org.ptss.support.api.dtos.requests.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.ptss.support.domain.constants.ValidationConstraints.PIN_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_PIN_CODE
import org.ptss.support.domain.constants.ValidationMessages.INVALID_PIN_CODE

data class PinLoginRequest(
    @field:NotBlank(message = EMPTY_PIN_CODE)
    @field:Pattern(
        regexp = PIN_PATTERN,
        message = INVALID_PIN_CODE
    )
    val pin: String
)