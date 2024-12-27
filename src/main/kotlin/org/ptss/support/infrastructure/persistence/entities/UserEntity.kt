package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity : BaseEntity() {
    @Column(name = "keycloak_id", nullable = false, unique = true, updatable = false)
    lateinit var keycloakId: UUID

    @Column(name = "first_name", nullable = false, length = 64)
    lateinit var firstName: String

    @Column(name = "last_name", nullable = false, length = 64)
    lateinit var lastName: String

    @Column(name = "last_seen", nullable = false)
    var lastSeen: OffsetDateTime = OffsetDateTime.now()

    // Navigate from User to their family memberships
    @OneToMany(mappedBy = "userId")
    val groupFamilyMemberships: Set<GroupFamilyMemberEntity> = HashSet()
}