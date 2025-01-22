package org.ptss.support.infrastructure.handlers.queries.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery
import org.ptss.support.domain.interfaces.queries.groups.IGetAllGroupsQueryHandler
import org.ptss.support.domain.models.Group
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import org.ptss.support.common.pagination.CursorPage
import io.quarkus.panache.common.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ApplicationScoped
class GetAllGroupsQueryHandler : IGetAllGroupsQueryHandler {
    override suspend fun handleAsync(query: GetAllGroupsQuery): CursorPage<Group> =
        withContext(Dispatchers.IO) {
            handleTransaction(query)
        }

    fun handleTransaction(query: GetAllGroupsQuery): CursorPage<Group> {
        Log.debug("Fetching groups page with limit: ${query.limit}, cursor: ${query.cursor}")

        val groups = if (query.cursor != null) {
            GroupEntity
                .find("id > ?1", query.cursor)
                .page(0, query.limit + 1)
                .list()
        } else {
            GroupEntity
                .findAll(Sort.by("id"))
                .page(0, query.limit + 1)
                .list()
        }

        val hasMore = groups.size > query.limit
        val items = groups.take(query.limit).map { it.toModel() }
        val nextCursor = if (hasMore && items.isNotEmpty()) items.last().id else null

        Log.debug("Retrieved ${items.size} groups, hasMore: $hasMore")
        return CursorPage(
            items = items,
            nextCursor = nextCursor
        )
    }
}