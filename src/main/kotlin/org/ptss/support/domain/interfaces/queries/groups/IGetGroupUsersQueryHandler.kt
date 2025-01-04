package org.ptss.support.domain.interfaces.queries.groups

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery

interface IGetGroupUsersQueryHandler : IQueryHandler<GetGroupUsersQuery, List<User>>