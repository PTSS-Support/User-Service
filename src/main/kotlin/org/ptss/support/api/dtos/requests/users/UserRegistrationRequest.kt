package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.ptss.support.domain.constants.ValidationConstraints.NAME_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.NAME_PATTERN
import org.ptss.support.domain.constants.ValidationConstraints.PASSWORD_PATTERN
import org.ptss.support.domain.constants.ValidationConstraints.VERIFICATION_CODE_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_VERIFICATION_CODE
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_FIRST_NAME
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_LAST_NAME_
import org.ptss.support.domain.constants.ValidationMessages.FIRST_NAME_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.LAST_NAME_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.FIRST_NAME_INVALID
import org.ptss.support.domain.constants.ValidationMessages.INVALID_PASSWORD
import org.ptss.support.domain.constants.ValidationMessages.INVALID_VERIFICATION_CODE
import org.ptss.support.domain.constants.ValidationMessages.LAST_NAME_INVALID

data class UserRegistrationRequest(
    @field:NotBlank(message = EMPTY_FIRST_NAME)
    @field:Size(
        max = NAME_MAX_LENGTH,
        message = FIRST_NAME_LENGTH
    )
    @field:Pattern(
        regexp = NAME_PATTERN,
        message = FIRST_NAME_INVALID
    )
    val firstName: String,

    @field:NotBlank(message = EMPTY_LAST_NAME_)
    @field:Size(
        max = NAME_MAX_LENGTH,
        message = LAST_NAME_LENGTH
    )
    @field:Pattern(
        regexp = NAME_PATTERN,
        message = LAST_NAME_INVALID
    )
    val lastName: String,

    @field:NotBlank(message = EMPTY_PASSWORD)
    @field:Pattern(
        regexp = PASSWORD_PATTERN,
        message = INVALID_PASSWORD
    )
    val password: String,

    @field:NotBlank(message = EMPTY_VERIFICATION_CODE)
    @field:Pattern(
        regexp = VERIFICATION_CODE_PATTERN,
        message = INVALID_VERIFICATION_CODE
    )
    val invitationCode: String
)