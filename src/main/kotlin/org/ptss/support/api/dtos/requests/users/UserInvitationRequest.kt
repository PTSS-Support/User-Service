package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserInvitationRequest(
    @field:NotBlank(message = "Email mag niet leeg zijn")
    @field:Email(message = "Vul een geldig email adres in")
    val email: String
)