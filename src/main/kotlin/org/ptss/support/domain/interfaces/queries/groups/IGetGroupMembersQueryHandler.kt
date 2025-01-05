package org.ptss.support.domain.interfaces.queries.groups

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery

interface IGetGroupMembersQueryHandler : IQueryHandler<GetGroupMembersQuery, List<User>>