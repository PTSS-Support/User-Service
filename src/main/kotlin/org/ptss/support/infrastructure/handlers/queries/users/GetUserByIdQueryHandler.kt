package org.ptss.support.infrastructure.handlers.queries.users

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.queries.users.GetUserByIdQuery
import org.ptss.support.domain.interfaces.queries.users.IGetUserByIdQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel

@ApplicationScoped
class GetUserByIdQueryHandler : IGetUserByIdQueryHandler {
    override suspend fun handleAsync(query: GetUserByIdQuery): User =
        withContext(Dispatchers.IO) {
            handleTransaction(query)
        }

    @Transactional
    fun handleTransaction(query: GetUserByIdQuery): User {
        Log.debug("Fetching user by ID: ${query.userId}")

        val user = UserEntity.findById(query.userId)
            ?: throw NotFoundException("User with ID ${query.userId} not found").also {
                Log.error("User not found with ID: ${query.userId}")
            }

        return user.toModel().also {
            Log.debug("Successfully retrieved user: ${it.id}")
        }
    }
}