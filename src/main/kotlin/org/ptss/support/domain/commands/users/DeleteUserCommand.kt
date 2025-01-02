package org.ptss.support.domain.commands.users

import java.util.UUID

data class DeleteUserCommand(
    val userId: UUID
)