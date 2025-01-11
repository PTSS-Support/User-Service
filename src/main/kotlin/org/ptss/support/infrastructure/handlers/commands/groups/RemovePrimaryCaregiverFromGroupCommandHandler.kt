package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.groups.RemovePrimaryCaregiverFromGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.IRemovePrimaryCaregiverFromGroupCommandHandler
import org.ptss.support.infrastructure.persistence.entities.GroupEntity

@ApplicationScoped
class RemovePrimaryCaregiverFromGroupCommandHandler : IRemovePrimaryCaregiverFromGroupCommandHandler {
    override suspend fun handleAsync(command: RemovePrimaryCaregiverFromGroupCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: RemovePrimaryCaregiverFromGroupCommand) {
        Log.debug("Attempting to remove primary caregiver from group ${command.groupId}")

        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${command.groupId}")
            }

        if (group.primaryCaregiver == null) {
            throw BadRequestException("Group does not have a primary caregiver assigned")
        }

        // Remove primary caregiver role but keep as family member
        group.removeAsPrimaryCaregiver(group.primaryCaregiver!!)
        group.persistAndFlush()

        Log.info("Successfully removed primary caregiver from group ${command.groupId}")
    }
}