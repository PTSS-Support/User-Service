package org.ptss.support.infrastructure.persistence.entities

import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*
import org.ptss.support.domain.constants.ValidationConstraints.NAME_MAX_LENGTH
import org.ptss.support.domain.enums.Role

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

    // For now, we will save this here, but this will be fetched from Keycloak in the future.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    lateinit var role: Role

    // Navigate from User to their family memberships
    @OneToMany(mappedBy = "user")
    val groupFamilyMemberships: Set<GroupFamilyMemberEntity> = HashSet()
}