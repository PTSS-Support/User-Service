package org.ptss.support.infrastructure.persistence.config

import io.quarkus.test.common.QuarkusTestResource
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@QuarkusTestResource(DatabaseTestResource::class)
abstract class BaseRepositoryTest {
    @PersistenceContext
    protected lateinit var entityManager: EntityManager

    protected fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}