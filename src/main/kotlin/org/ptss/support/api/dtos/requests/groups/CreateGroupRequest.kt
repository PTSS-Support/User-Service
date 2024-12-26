package org.ptss.support.api.dtos.requests.groups

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateGroupRequest(
    @field:NotBlank(message = "Zorgverlener ID mag niet leeg zijn")
    @field:Pattern(
        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
        message = "Zorgverlener ID moet een geldig UUID zijn"
    )
    val healthcareProfessionalId: String,

    @field:NotBlank(message = "Patiënt email mag niet leeg zijn")
    @field:Email(message = "Patiënt email moet een geldig mailadres zijn")
    val patientEmail: String,

    @field:Email(message = "Mantelzorger email moet een geldig mailadres zijn")
    val primaryCaregiverEmail: String? = null
)
