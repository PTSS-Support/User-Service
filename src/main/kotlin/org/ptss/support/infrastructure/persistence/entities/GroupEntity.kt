package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanionBase
import jakarta.persistence.*
import org.hibernate.annotations.Check
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.UUID
import kotlin.collections.HashSet

@Entity
@Table(
    name = "groups",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["patient_id"]),
        UniqueConstraint(columnNames = ["primary_caregiver_id"])
    ]
)
// Ensures patient, healthcare professional, and caregiver IDs are distinct in the database
@Check(
    constraints = """
        patient_id != healthcare_professional_id AND 
        patient_id != primary_caregiver_id AND 
        healthcare_professional_id != primary_caregiver_id
    """
)
class GroupEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // When patient is deleted, delete the group
    var patient: UserEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT) // Prevent deletion of healthcare professional if in group
    lateinit var healthcareProfessional: UserEntity

    // PrimaryCaregiver is just a reference to one of the family members
    // No special deletion behavior needed since it's just a reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var primaryCaregiver: UserEntity? = null

    @OneToMany(
        mappedBy = "group",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val familyMembers: Set<GroupFamilyMemberEntity> = HashSet()

    val isActive: Boolean
        get() = patient != null

    @PreUpdate
    @PrePersist
    fun validateBusinessRules() {
        if (!isActive && familyMembers.isNotEmpty()) {
            throw IllegalStateException("Cannot have family members in inactive group")
        }
    }

    @PreRemove
    fun validateDeletion() {
        // Only allow deletion of groups through patient deletion
        // or if group is inactive (no patient)
        if (patient != null && !isMarkedForDeletion(patient!!)) {
            throw IllegalStateException("Cannot delete active group unless through patient deletion")
        }
    }

    private fun isMarkedForDeletion(user: UserEntity): Boolean {
        return try {
            findById(user.id) == null
        } catch (e: Exception) {
            true
        }
    }

    // Method to promote family member to primary caregiver
    fun promoteToPrimaryCaregiver(familyMember: GroupFamilyMemberEntity) {
        require(familyMembers.contains(familyMember)) { "User must be a family member first" }
        primaryCaregiver = familyMember.user
    }

    fun removeAsPrimaryCaregiver(user: UserEntity) {
        if (primaryCaregiver?.id == user.id) {
            primaryCaregiver = null
            persist()
        }
    }

    companion object : PanacheCompanionBase<GroupEntity, UUID>
}