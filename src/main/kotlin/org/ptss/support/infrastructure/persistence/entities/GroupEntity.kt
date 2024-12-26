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
@Check(
    constraints = """
        patient_id != healthcare_prof_id AND 
        patient_id != caregiver_id AND 
        healthcare_prof_id != caregiver_id
    """
)
class GroupEntity : BaseEntity() {
    @Id
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "patient_id", nullable = false)
    lateinit var patientId: UUID

    @Column(name = "healthcare_prof_id", nullable = false)
    lateinit var healthcareProfessionalId: UUID

    @Column(name = "primary_caregiver_id")
    var primaryCaregiverId: UUID? = null
}