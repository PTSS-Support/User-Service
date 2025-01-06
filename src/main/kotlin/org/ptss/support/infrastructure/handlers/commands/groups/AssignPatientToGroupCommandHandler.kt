package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.IAssignPatientToGroupCommandHandler
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ApplicationScoped
class AssignPatientToGroupCommandHandler : IAssignPatientToGroupCommandHandler {

    override suspend fun handleAsync(command: AssignPatientToGroupCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    fun handleTransaction(command: AssignPatientToGroupCommand) {
        Log.debug("Attempting to assign patient ${command.patientId} to group ${command.groupId}")

        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${command.groupId}")
            }

        val patient = UserEntity.findById(command.patientId)
            ?: throw NotFoundException("Patient not found").also {
                Log.error("Patient not found with ID: ${command.patientId}")
            }

        if (group.patient != null) {
            Log.warn("Attempted to assign patient to group ${command.groupId} that already has a patient")
            throw BadRequestException("Group already has a patient assigned")
        }

        group.patient = patient
        group.persistAndFlush()
        Log.info("Successfully assigned patient ${command.patientId} to group ${command.groupId}")
    }
}