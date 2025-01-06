package org.ptss.support.infrastructure.handlers.commands.groups

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.groups.AssignPatientToGroupCommand
import org.ptss.support.domain.interfaces.commands.groups.IAssignPatientToGroupCommandHandler
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class AssignPatientToGroupCommandHandler(
    private val em: EntityManager
) : IAssignPatientToGroupCommandHandler {

    @Transactional
    override suspend fun handleAsync(command: AssignPatientToGroupCommand) {
        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found")

        val patient = UserEntity.findById(command.patientId)
            ?: throw NotFoundException("Patient not found")

        if (group.patient != null) {
            throw BadRequestException("Group already has a patient assigned")
        }

        group.patient = patient
        group.persistAndFlush()
    }
}