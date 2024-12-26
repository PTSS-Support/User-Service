package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserInvitationVerificationRequest(
    @field:NotBlank(message = "Email mag niet leeg zijn")
    @field:Email(message = "Vul een geldig email adres in")
    val email: String,

    @field:NotBlank(message = "Uitnodigingscode mag niet leeg zijn")
    @field:Pattern(
        regexp = "^[0-9]{6}$",
        message = "Uitnodigingscode moet exact 6 cijfers bevatten"
    )
    val invitationCode: String
)
