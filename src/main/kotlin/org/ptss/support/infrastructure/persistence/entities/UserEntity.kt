package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity : BaseEntity() {
    @Column(nullable = false, unique = true, updatable = false)
    lateinit var keycloakId: UUID

    @Column(nullable = false, length = 64)
    lateinit var firstName: String

    @Column(nullable = false, length = 64)
    lateinit var lastName: String

    @Column(nullable = false)
    var lastSeen: OffsetDateTime = OffsetDateTime.now()

    // Navigate from User to their family memberships
    @OneToMany(mappedBy = "user")
    val groupFamilyMemberships: Set<GroupFamilyMemberEntity> = HashSet()
}