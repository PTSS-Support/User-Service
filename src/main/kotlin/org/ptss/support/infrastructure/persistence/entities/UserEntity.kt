package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanionBase
import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*
import org.ptss.support.domain.constants.ValidationConstraints.NAME_MAX_LENGTH
import org.ptss.support.domain.enums.Role

@Entity
@Table(name = "users")
class UserEntity : BaseEntity() {
    @Column(nullable = false, unique = true, updatable = false)
    lateinit var keycloakId: UUID

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    lateinit var firstName: String

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    lateinit var lastName: String

    @Column(nullable = false)
    var lastSeen: OffsetDateTime = OffsetDateTime.now()

    // For now, we will save this here, but this will be fetched from Keycloak in the future.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    lateinit var role: Role

    // Navigate from User to their family memberships
    @OneToMany(mappedBy = "user")
    val groupFamilyMemberships: Set<GroupFamilyMemberEntity> = HashSet()

    fun canDelete(targetUser: UserEntity): Boolean {
        // Self-deletion check
        if (this.id == targetUser.id) {
            return false
        }

        return when (this.role) {
            Role.ADMIN -> true  // Admin can delete anyone
            Role.HCP -> canHcpDelete(targetUser)
            Role.PATIENT -> canPatientDelete(targetUser)
            Role.PRIMARY_CAREGIVER -> canPrimaryCaregiverDelete(targetUser)
            Role.FAMILY_MEMBER -> false  // Family members can't delete anyone
        }
    }

    private fun canHcpDelete(targetUser: UserEntity): Boolean {
        return targetUser.role == Role.PATIENT &&
                GroupEntity.find("healthcareProfessional = ?1 and patient = ?2",
                    this, targetUser).count() > 0
    }

    private fun canPatientDelete(targetUser: UserEntity): Boolean {
        if (targetUser.role !in setOf(Role.FAMILY_MEMBER, Role.PRIMARY_CAREGIVER)) {
            return false
        }

        val targetGroup = targetUser.groupFamilyMemberships.firstOrNull()?.group
        return targetGroup?.patient?.id == this.id
    }

    private fun canPrimaryCaregiverDelete(targetUser: UserEntity): Boolean {
        if (targetUser.role != Role.FAMILY_MEMBER) {
            return false
        }

        val targetGroup = targetUser.groupFamilyMemberships.firstOrNull()?.group
        return targetGroup?.primaryCaregiver?.id == this.id
    }

    @PreRemove
    fun validateDeletion() {
        when (role) {
            Role.ADMIN -> validateAdminDeletion()
            Role.HCP -> validateHcpDeletion()
            Role.PATIENT -> validatePatientDeletion()
            Role.PRIMARY_CAREGIVER, Role.FAMILY_MEMBER -> validateFamilyMemberDeletion()
        }
    }

    private fun validateAdminDeletion() {
        // Prevent deletion of last admin
        val adminCount = count("role", Role.ADMIN)
        if (adminCount <= 1) {
            throw IllegalStateException("Cannot delete last administrator")
        }
    }

    private fun validateHcpDeletion() {
        // Ensure HCP is not part of a group
        val hasGroups = GroupEntity.find("healthcareProfessional", this).count() > 0
        if (hasGroups) {
            throw IllegalStateException("Cannot delete healthcare professional with active groups")
        }
    }

    private fun validatePatientDeletion() {
        // Ensure patient has a group before deletion
        GroupEntity.find("patient", this).firstResult()
            ?: throw IllegalStateException("Patient must have a group")
    }

    private fun validateFamilyMemberDeletion() {
        // Only allow actual deletion if it's part of a patient deletion cascade
        val group = groupFamilyMemberships.firstOrNull()?.group
        if (group?.patient != null) {
            throw IllegalStateException("Family members can only be anonymized, not deleted")
        }
    }

    companion object : PanacheCompanionBase<UserEntity, UUID>
}