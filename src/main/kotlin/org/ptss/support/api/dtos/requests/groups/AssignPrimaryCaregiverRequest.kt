package org.ptss.support.api.dtos.requests.groups

import jakarta.validation.constraints.NotBlank
import org.ptss.support.domain.constants.ValidationMessages.EMPTY_MEMBER_ID
import java.util.UUID

data class AssignPrimaryCaregiverRequest(
    @field:NotBlank(message = EMPTY_MEMBER_ID)
    val memberId: UUID
)