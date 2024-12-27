package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import jakarta.persistence.*
import org.hibernate.annotations.Check

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
        patient_id != healthcare_prof_id AND 
        patient_id != caregiver_id AND 
        healthcare_prof_id != caregiver_id
    """
)
class GroupEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, updatable = false)
    lateinit var patient: UserEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "healthcare_prof_id", nullable = false)
    lateinit var healthcareProfessional: UserEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_caregiver_id")
    var primaryCaregiver: UserEntity? = null

    @OneToMany(mappedBy = "group")
    val familyMembers: Set<GroupFamilyMemberEntity> = HashSet()
}