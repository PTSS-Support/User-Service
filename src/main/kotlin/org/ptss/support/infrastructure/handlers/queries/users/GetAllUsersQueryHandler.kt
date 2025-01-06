package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetAllUsersQuery
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.queries.users.IGetAllUsersQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort

@ApplicationScoped
class GetAllUsersQueryHandler : IGetAllUsersQueryHandler {
    override suspend fun handleAsync(query: GetAllUsersQuery): CursorPage<User> {
        val limit = query.limit ?: DEFAULT_PAGE_SIZE

        val users = if (query.cursor != null) {
            UserEntity
                .find("id > ?1", query.cursor)
                .page(0, limit + 1)
                .list()
        } else {
            UserEntity
                .findAll(Sort.by("id"))
                .page(0, limit + 1)
                .list()
        }

        val hasMore = users.size > limit
        val items = users.take(limit).map { it.toModel() }
        val nextCursor = if (hasMore && items.isNotEmpty()) items.last().id else null

        return CursorPage(
            items = items,
            nextCursor = nextCursor
        )
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}