package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(
    name = "group_family_members",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user"])
    ]
)
class GroupFamilyMemberEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    lateinit var group: GroupEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    lateinit var user: UserEntity

    @Column
    var anonymizedAt: OffsetDateTime? = null

    companion object : PanacheCompanion<GroupFamilyMemberEntity> {
        fun anonymize(member: GroupFamilyMemberEntity) {
            member.anonymizedAt = OffsetDateTime.now()
            member.user.firstName = "[VERWIJDERD]"
            member.user.lastName = "[VERWIJDERD]"
            member.persist()
        }
    }
}