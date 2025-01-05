package org.ptss.support.domain.interfaces.queries.users

import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.domain.queries.users.GetCurrentUserQuery

interface IGetCurrentUserQueryHandler : IQueryHandler<GetCurrentUserQuery, User>