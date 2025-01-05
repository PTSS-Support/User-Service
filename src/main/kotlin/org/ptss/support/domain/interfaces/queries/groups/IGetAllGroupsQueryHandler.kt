package org.ptss.support.domain.interfaces.queries.groups

import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Group
import org.ptss.support.domain.queries.groups.GetAllGroupsQuery

interface IGetAllGroupsQueryHandler : IQueryHandler<GetAllGroupsQuery, CursorPage<Group>>