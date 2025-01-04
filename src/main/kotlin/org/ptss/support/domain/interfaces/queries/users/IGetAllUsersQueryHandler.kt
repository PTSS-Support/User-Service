package org.ptss.support.domain.interfaces.queries.users

import org.ptss.support.common.pagination.CursorPage
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.domain.queries.users.GetAllUsersQuery

interface IGetAllUsersQueryHandler : IQueryHandler<GetAllUsersQuery, CursorPage<User>>