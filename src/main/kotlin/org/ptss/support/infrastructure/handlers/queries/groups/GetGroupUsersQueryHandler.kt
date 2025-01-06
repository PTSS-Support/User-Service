package org.ptss.support.infrastructure.handlers.queries.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.queries.groups.GetGroupUsersQuery
import org.ptss.support.domain.interfaces.queries.groups.IGetGroupUsersQueryHandler
import org.ptss.support.domain.models.User
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import jakarta.ws.rs.NotFoundException

@ApplicationScoped
class GetGroupUsersQueryHandler : IGetGroupUsersQueryHandler {
    override suspend fun handleAsync(query: GetGroupUsersQuery): List<User> {
        Log.debug("Fetching all users for group ID: ${query.groupId}")

        val group = GroupEntity.findById(query.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${query.groupId}")
            }

        return buildList {
            group.patient?.let {
                add(it.toModel())
                Log.debug("Added patient user to group ${query.groupId}")
            }
            add(group.healthcareProfessional.toModel())
            Log.debug("Added HCP user to group ${query.groupId}")

            addAll(group.familyMembers.map { it.user.toModel() })
            Log.debug("Added ${group.familyMembers.size} family member users to group ${query.groupId}")
        }
    }
}