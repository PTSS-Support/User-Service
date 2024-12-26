package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import java.util.UUID
import java.time.OffsetDateTime
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity : PanacheEntityBase {
    @Id
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "keycloak_id", nullable = false, unique = true)
    lateinit var keycloakId: String

    @Column(name = "first_name", nullable = false)
    lateinit var firstName: String

    @Column(name = "last_name", nullable = false)
    lateinit var lastName: String

    @Column(name = "last_seen")
    var lastSeen: OffsetDateTime? = null
}