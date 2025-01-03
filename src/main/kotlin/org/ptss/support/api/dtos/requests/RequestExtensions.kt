// Extension functions for converting request DTOs to commands
// Unlike response/entity mappings, request mappings often require additional context/parameters
// This is kept separate from the DTOs to:
// 1. Keep DTOs as pure data carriers
// 2. Avoid circular dependencies between domains and DTOs
// 3. Centralize all request mapping logic in one place
// 4. Allow for complex transformations with additional parameters
package org.ptss.support.api.dtos.requests

import org.ptss.support.api.dtos.requests.groups.CreateGroupRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationRequest
import org.ptss.support.api.dtos.requests.invitations.UserInvitationVerificationRequest
import org.ptss.support.api.dtos.requests.invitations.UserRegistrationRequest
import org.ptss.support.domain.commands.groups.CreateGroupCommand
import org.ptss.support.domain.commands.invitations.CreateInvitationCommand
import org.ptss.support.domain.commands.invitations.RegisterUserCommand
import org.ptss.support.domain.commands.invitations.VerifyInvitationCommand
import org.ptss.support.domain.enums.Role
import java.util.UUID

fun CreateGroupRequest.toCreateGroupCommand() = CreateGroupCommand(
    healthcareProfessionalId = this.healthcareProfessionalId
)

fun CreateGroupRequest.toCreateInvitationCommand(groupId: UUID) = CreateInvitationCommand(
    email = this.patientEmail,
    role = Role.PATIENT, // Patient role for group creation invitations
    groupId = groupId
)

fun UserInvitationRequest.toCommand(groupId: UUID) = CreateInvitationCommand(
    email = this.email,
    groupId = groupId,
    role = Role.FAMILY_MEMBER // Default role for regular invitations
)

fun UserInvitationVerificationRequest.toCommand() = VerifyInvitationCommand(
    email = this.email,
    verificationCode = this.invitationCode
)

fun UserRegistrationRequest.toCommand() = RegisterUserCommand(
    firstName = this.firstName,
    lastName = this.lastName,
    password = this.password,
    invitationCode = this.invitationCode
)