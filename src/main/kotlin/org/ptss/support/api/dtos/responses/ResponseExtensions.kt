// Extension functions for converting domain models to response DTOs
// This is kept separate from the DTOs to:
// 1. Keep DTOs as pure data carriers without business logic
// 2. Avoid circular dependencies between domains and DTOs
// 3. Centralize all response mapping logic in one place
// 4. Follow Kotlin's extension function design philosophy for cross-boundary mapping
package org.ptss.support.api.dtos.responses

import org.ptss.support.api.dtos.responses.groups.GroupResponse
import org.ptss.support.api.dtos.responses.invitations.InvitationResponse
import org.ptss.support.api.dtos.responses.users.UserResponse
import org.ptss.support.domain.models.Group
import org.ptss.support.domain.models.Invitation
import org.ptss.support.domain.models.User

fun Group.toResponse() = GroupResponse(
    id = this.id,
    patientId = this.patientId,
    healthcareProfessionalId = this.healthcareProfessionalId,
    primaryCaregiverId = this.primaryCaregiverId
)

fun Invitation.toResponse() = InvitationResponse(
    email = this.email,
    role = this.role,
    groupId = this.groupId,
    isRegistered = this.isRegistered
)

fun User.toResponse() = UserResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    lastSeen = this.lastSeen,
    groupId = this.groupId
)

// Collection mappings
fun List<Group>.toGroupResponse() = this.map { it.toResponse() }
fun List<Invitation>.toInvitationResponse() = this.map { it.toResponse() }
fun List<User>.toUserResponse() = this.map { it.toResponse() }