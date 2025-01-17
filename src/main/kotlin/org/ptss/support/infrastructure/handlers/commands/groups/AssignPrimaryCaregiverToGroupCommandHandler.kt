package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.groups.AssignPrimaryCaregiverToGroupCommand
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.commands.groups.IAssignPrimaryCaregiverToGroupCommandHandler
import org.ptss.support.domain.models.Group
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.AuthUpdateRoleRequest
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.GroupFamilyMemberEntity
import org.ptss.support.infrastructure.persistence.entities.toModel
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync

@ApplicationScoped
class AssignPrimaryCaregiverToGroupCommandHandler(
    private val authenticationServiceClient: IAuthenticationServiceClient
) : IAssignPrimaryCaregiverToGroupCommandHandler {
    override suspend fun handleAsync(command: AssignPrimaryCaregiverToGroupCommand): Group {
        // First handle the database transaction
        val (group, familyMember) = withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

        // Then update the role in the authentication service
        executeWithExceptionLoggingAsync(
            operation = {
                authenticationServiceClient.updateRole(
                    id = familyMember.user.keycloakId.toString(),
                    request = AuthUpdateRoleRequest(role = Role.PRIMARY_CAREGIVER)
                )
            },
            logMessage = "Failed to update user role to PRIMARY_CAREGIVER in authentication service"
        )

        return group
    }

    @Transactional
    fun handleTransaction(command: AssignPrimaryCaregiverToGroupCommand): Pair<Group, GroupFamilyMemberEntity> {
        Log.debug("Attempting to assign primary caregiver ${command.memberId} to group ${command.groupId}")

        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${command.groupId}")
            }

        // Check if the group already has a primary caregiver
        if (group.primaryCaregiver != null) {
            throw BadRequestException("Group already has a primary caregiver assigned")
        }

        // Find the family member entity
        val familyMember = GroupFamilyMemberEntity
            .find("group.id = ?1 and user.id = ?2", command.groupId, command.memberId)
            .firstResult() ?: throw BadRequestException("User must be a family member of the group first")

        // Promote to primary caregiver
        group.promoteToPrimaryCaregiver(familyMember)
        group.persistAndFlush()

        Log.info("Successfully assigned primary caregiver ${command.memberId} to group ${command.groupId}")
        return Pair(group.toModel(), familyMember)
    }
}