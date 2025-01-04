package org.ptss.support.infrastructure.handlers.queries.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.models.Group

@ApplicationScoped
class GetAllGroupsQueryHandler : IQueryHandler<GetAllGroupsQuery, CursorPage<Group>> {
    override suspend fun handleAsync(query: GetAllGroupsQuery): CursorPage<Group> {
        throw NotImplementedError("GetAllGroupsQueryHandler not implemented yet.")
    }
}