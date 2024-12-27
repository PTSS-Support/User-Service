package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import java.time.OffsetDateTime
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@MappedSuperclass
abstract class BaseEntity : PanacheEntityBase {
    @Id
    @Column(name = "id")
    lateinit var id: UUID

    // Database-managed
    // Primarily used for auditing purposes
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: OffsetDateTime

    // Database-managed
    // Primarily used for auditing purposes
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: OffsetDateTime
}