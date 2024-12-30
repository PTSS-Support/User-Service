package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*
import org.ptss.support.domain.constants.ValidationConstraints.NAME_MAX_LENGTH

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

    // Navigate from User to their family memberships
    @OneToMany(mappedBy = "user")
    val groupFamilyMemberships: Set<GroupFamilyMemberEntity> = HashSet()
}