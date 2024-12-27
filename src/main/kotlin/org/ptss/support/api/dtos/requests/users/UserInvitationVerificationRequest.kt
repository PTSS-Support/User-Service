package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.VERIFICATION_CODE_PATTERN
import org.ptss.support.domain.constants.ValidationMessages.EMAIL_LENGTH
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_EMAIL
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_VERIFICATION_CODE
import org.ptss.support.domain.constants.ValidationMessages.INVALID_EMAIL
import org.ptss.support.domain.constants.ValidationMessages.INVALID_VERIFICATION_CODE

data class UserInvitationVerificationRequest(
    @field:NotBlank(message = EMPTY_EMAIL)
    @field:Email(message = INVALID_EMAIL)
    @field:Size(
        max = EMAIL_MAX_LENGTH,
        message = EMAIL_LENGTH
    )
    val email: String,

    @field:NotBlank(message = EMPTY_VERIFICATION_CODE)
    @field:Pattern(
        regexp = VERIFICATION_CODE_PATTERN,
        message = INVALID_VERIFICATION_CODE
    )
    val invitationCode: String
)
