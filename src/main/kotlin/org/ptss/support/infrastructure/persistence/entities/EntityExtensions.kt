// Extension functions for the entities
// This is kept separate from the entities because this is a business concern and entities should focus on data concerns
// This also adheres to Kotlin's design philosophy, the Open/Closed Principle, and avoids circular dependencies
package org.ptss.support.infrastructure.persistence.entities

import org.ptss.support.domain.models.Group
import org.ptss.support.domain.models.GroupFamilyMember
import org.ptss.support.domain.models.Invitation
import org.ptss.support.domain.models.User

// Extension function for GroupEntity
fun GroupEntity.toModel() = Group(
    id = this.id,
    patientId = this.patient?.id,
    healthcareProfessionalId = this.healthcareProfessional.id,
    primaryCaregiverId = this.primaryCaregiver?.id,
    familyMembers = this.familyMembers.map { it.toModel() }.toSet(),
    isActive = this.isActive
)

// Extension function for GroupFamilyMemberEntity
fun GroupFamilyMemberEntity.toModel() = GroupFamilyMember(
    id = this.id,
    groupId = this.group.id,
    userId = this.user.id,
    anonymizedAt = this.anonymizedAt
)

// Extension function for InvitationEntity
fun InvitationEntity.toModel() = Invitation(
    id = this.id,
    email = this.email,
    role = this.role,
    verificationCode = this.verificationCode,
    groupId = this.groupId,
    expiresAt = this.expiresAt,
    isVerified = this.isVerified,
    isRegistered = this.isRegistered
)

// Extension function for UserEntity
fun UserEntity.toModel() = User(
    id = this.id,
    keycloakId = this.keycloakId.toString(),
    firstName = this.firstName,
    lastName = this.lastName,
    lastSeen = this.lastSeen,
    groupId = this.groupFamilyMemberships.firstOrNull()?.group?.id,
    role = this.role
)