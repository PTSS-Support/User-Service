package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.groups.CreateGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.ICreateGroupCommandHandler
import org.ptss.support.domain.models.Group
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import jakarta.ws.rs.NotFoundException
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ApplicationScoped
class CreateGroupCommandHandler : ICreateGroupCommandHandler {

    override suspend fun handleAsync(command: CreateGroupCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: CreateGroupCommand): Group {
        Log.debug("Attempting to create group with HCP ID: ${command.healthcareProfessionalId}")

        val hcp = UserEntity.findById(command.healthcareProfessionalId)
            ?: throw NotFoundException("Healthcare professional not found").also {
                Log.error("Healthcare professional not found with ID: ${command.healthcareProfessionalId}")
            }

        val group = GroupEntity().apply {
            healthcareProfessional = hcp
        }

        group.persistAndFlush()
        Log.info("Successfully created group with ID: ${group.id} for HCP: ${command.healthcareProfessionalId}")
        return group.toModel()
    }
}