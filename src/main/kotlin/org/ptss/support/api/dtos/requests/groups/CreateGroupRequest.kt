package org.ptss.support.api.dtos.requests.groups

import java.util.UUID
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_HEALTHCARE_PROFESSIONAL_ID
import org.ptss.support.domain.constants.ValidationMessages.PATIENT_EMAIL_EMPTY
import org.ptss.support.domain.constants.ValidationMessages.PATIENT_EMAIL_INVALID

data class CreateGroupRequest(
    @field:NotBlank(message = EMPTY_HEALTHCARE_PROFESSIONAL_ID)
    val healthcareProfessionalId: UUID,

    @field:NotBlank(message = PATIENT_EMAIL_EMPTY)
    @field:Email(message = PATIENT_EMAIL_INVALID)
    val patientEmail: String,
)
