package org.ptss.support.infrastructure.handlers.queries.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetGroupMembersQuery
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupMembersQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import jakarta.ws.rs.NotFoundException
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupUsersQueryHandler
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery

@ApplicationScoped
class GetGroupMembersQueryHandler(
    private val getGroupUsersQueryHandler: IGetGroupUsersQueryHandler
) : IGetGroupMembersQueryHandler {
    override suspend fun handleAsync(query: GetGroupMembersQuery): List<User> {
        Log.debug("Fetching members for group ID: ${query.groupId}")

        val group = GroupEntity.findById(query.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${query.groupId}")
            }

        val allUsers = getGroupUsersQueryHandler.handleAsync(GetGroupUsersQuery(query.groupId))
        val members = allUsers.filter { it.id != group.healthcareProfessional.id }

        Log.debug("Retrieved ${members.size} members for group ${query.groupId}")
        return members
    }
}