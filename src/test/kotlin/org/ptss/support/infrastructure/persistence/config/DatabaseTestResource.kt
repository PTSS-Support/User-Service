package org.ptss.support.infrastructure.persistence.config

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class DatabaseTestResource : QuarkusTestResourceLifecycleManager {
    companion object {
        private val postgres: PostgreSQLContainer<*> by lazy {
            PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test")
        }
    }

    override fun start(): Map<String, String> {
        postgres.start()

        return mapOf(
            "quarkus.datasource.jdbc.url" to postgres.jdbcUrl,
            "quarkus.datasource.username" to postgres.username,
            "quarkus.datasource.password" to postgres.password
        )
    }

    override fun stop() {
        if (postgres.isRunning) {
            postgres.stop()
        }
    }
}