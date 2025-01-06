package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotFoundException
import org.ptss.support.domain.queries.users.GetUserByIdQuery
import org.ptss.support.domain.interfaces.queries.users.IGetUserByIdQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel

@ApplicationScoped
class GetUserByIdQueryHandler : IGetUserByIdQueryHandler {
    override suspend fun handleAsync(query: GetUserByIdQuery): User {
        val user = UserEntity.findById(query.userId)
            ?: throw NotFoundException("User with ID ${query.userId} not found")

        return user.toModel()
    }
}