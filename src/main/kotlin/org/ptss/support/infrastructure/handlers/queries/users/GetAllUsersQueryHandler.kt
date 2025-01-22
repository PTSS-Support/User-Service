package org.ptss.support.infrastructure.handlers.queries.users

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.users.GetAllUsersQuery
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.queries.users.IGetAllUsersQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import io.quarkus.panache.common.Sort
import io.quarkus.logging.Log
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ApplicationScoped
class GetAllUsersQueryHandler : IGetAllUsersQueryHandler {
    override suspend fun handleAsync(query: GetAllUsersQuery): CursorPage<User> =
        withContext(Dispatchers.IO) {
            handleTransaction(query)
        }

    @Transactional
    fun handleTransaction(query: GetAllUsersQuery): CursorPage<User> {
        Log.debug("Fetching users page with limit: ${query.limit}, cursor: ${query.cursor}")

        val users = if (query.cursor != null) {
            UserEntity
                .find("id > ?1", query.cursor)
                .page(0, query.limit + 1)
                .list()
        } else {
            UserEntity
                .findAll(Sort.by("id"))
                .page(0, query.limit + 1)
                .list()
        }

        val hasMore = users.size > query.limit
        val items = users.take(query.limit).map { it.toModel() }
        val nextCursor = if (hasMore && items.isNotEmpty()) items.last().id else null

        Log.debug("Retrieved ${items.size} users, hasMore: $hasMore")
        return CursorPage(
            items = items,
            nextCursor = nextCursor
        )
    }
}