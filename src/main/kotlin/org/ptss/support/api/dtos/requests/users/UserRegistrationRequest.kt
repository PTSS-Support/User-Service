package org.ptss.support.api.dtos.requests.users

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRegistrationRequest(
    @field:NotBlank(message = "Voornaam mag niet leeg zijn")
    @field:Size(
        min = 1,
        max = 64,
        message = "Voornaam moet tussen 1 en 64 karakters zijn"
    )
    @field:Pattern(
        regexp = "^[\\p{L}\\p{M}' .-]+$",
        message = "Voornaam mag alleen letters, spaties, koppeltekens en apostroffen bevatten"
    )
    val firstName: String,

    @field:NotBlank(message = "Achternaam mag niet leeg zijn")
    @field:Size(
        min = 1,
        max = 64,
        message = "Achternaam moet tussen 1 en 64 karakters zijn"
    )
    @field:Pattern(
        regexp = "^[\\p{L}\\p{M}' .-]+$",
        message = "Achternaam mag alleen letters, spaties, koppeltekens en apostroffen bevatten"
    )
    val lastName: String,

    @field:NotBlank(message = "Wachtwoord mag niet leeg zijn")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?])(?=\\S+$).{9,}$",
        message = "Wachtwoord moet minimaal 9 karakters bevatten, waaronder minimaal 1 kleine letter, " +
                "1 hoofdletter, 1 cijfer en 1 speciaal karakter"
    )
    val password: String,

    @field:NotBlank(message = "Uitnodigingscode mag niet leeg zijn")
    @field:Pattern(
        regexp = "^[0-9]{6}$",
        message = "Uitnodigingscode moet exact 6 cijfers bevatten"
    )
    val invitationCode: String
)