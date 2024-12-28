package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.hibernate.annotations.Check
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "groups",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["patient"]),
        UniqueConstraint(columnNames = ["primaryCaregiver"])
    ]
)
// Ensures patient, healthcare professional, and caregiver IDs are distinct in the database
@Check(
    constraints = """
        patient_id != healthcare_prof_id AND 
        patient_id != caregiver_id AND 
        healthcare_prof_id != caregiver_id
    """
)
class GroupEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // When patient is deleted, delete the group
    lateinit var patient: UserEntity

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

    // Method to promote family member to primary caregiver
    fun promoteToPrimaryCaregiver(familyMember: GroupFamilyMemberEntity) {
        require(familyMembers.contains(familyMember)) { "User must be a family member first" }
        primaryCaregiver = familyMember.user
    }
}