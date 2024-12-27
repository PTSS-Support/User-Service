package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "group_family_members",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id"])
    ]
)
class GroupFamilyMemberEntity : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, updatable = false)
    lateinit var group: GroupEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    lateinit var user: UserEntity
}