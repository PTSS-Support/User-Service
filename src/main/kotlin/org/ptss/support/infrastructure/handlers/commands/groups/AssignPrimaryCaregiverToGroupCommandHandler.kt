package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.groups.AssignPrimaryCaregiverToGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.IAssignPrimaryCaregiverToGroupCommandHandler
import org.ptss.support.domain.models.Group
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.GroupFamilyMemberEntity
import org.ptss.support.infrastructure.persistence.entities.toModel

@ApplicationScoped
class AssignPrimaryCaregiverToGroupCommandHandler : IAssignPrimaryCaregiverToGroupCommandHandler {
    override suspend fun handleAsync(command: AssignPrimaryCaregiverToGroupCommand): Group =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: AssignPrimaryCaregiverToGroupCommand): Group {
        Log.debug("Attempting to assign primary caregiver ${command.memberId} to group ${command.groupId}")

        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${command.groupId}")
            }

        // Check if the user is already a primary caregiver
        if (group.primaryCaregiver != null) {
            throw BadRequestException("Group already has a primary caregiver assigned")
        }

        // Find the family member entity
        val familyMember = GroupFamilyMemberEntity
            .find("group.id = ?1 and user.id = ?2", command.groupId, command.memberId)
            .firstResult() ?: throw BadRequestException("User must be a family member first")

        // Promote to primary caregiver
        group.promoteToPrimaryCaregiver(familyMember)
        group.persistAndFlush()

        Log.info("Successfully assigned primary caregiver ${command.memberId} to group ${command.groupId}")
        return group.toModel()
    }
}