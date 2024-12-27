package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import jakarta.persistence.*
import org.hibernate.annotations.Check

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
    lateinit var patient: UserEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    lateinit var healthcareProfessional: UserEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var primaryCaregiver: UserEntity? = null

    @OneToMany(mappedBy = "group")
    val familyMembers: Set<GroupFamilyMemberEntity> = HashSet()
}