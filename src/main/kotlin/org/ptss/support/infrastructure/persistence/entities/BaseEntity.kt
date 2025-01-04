package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import java.time.OffsetDateTime
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity : PanacheEntityBase {
    // Optimistic locking with @Version
    // Prevents concurrent modifications by different transactions
    // Throws OptimisticLockException which should be handled by a retry with an exponential backoff strategy
    @Version
    var version: Long = 0

    @Id
    lateinit var id: UUID

    // Database-managed
    // Primarily used for auditing purposes
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    // Database-managed
    // Primarily used for auditing purposes
    @UpdateTimestamp
    lateinit var updatedAt: OffsetDateTime
}