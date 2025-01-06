package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanionBase
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.ptss.support.domain.constants.AnonymizationConstants.ANONYMIZED_VALUE
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(
    name = "group_family_members",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id"])
    ]
)
class GroupFamilyMemberEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    lateinit var group: GroupEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    lateinit var user: UserEntity

    @Column
    var anonymizedAt: OffsetDateTime? = null

    companion object : PanacheCompanionBase<GroupFamilyMemberEntity, UUID> {
        fun anonymize(member: GroupFamilyMemberEntity) {
            member.anonymizedAt = OffsetDateTime.now()
            member.user.firstName = ANONYMIZED_VALUE
            member.user.lastName = ANONYMIZED_VALUE
            member.persist()
        }
    }
}