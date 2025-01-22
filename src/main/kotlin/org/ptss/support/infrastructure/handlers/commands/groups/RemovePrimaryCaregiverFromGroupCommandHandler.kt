package org.ptss.support.infrastructure.handlers.commands.groups

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.groups.RemovePrimaryCaregiverFromGroupCommand
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.commands.groups.IRemovePrimaryCaregiverFromGroupCommandHandler
import org.ptss.support.infrastructure.external_services.auth.clients.IAuthenticationServiceClient
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.AuthUpdateRoleRequest
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import java.util.UUID

@ApplicationScoped
class RemovePrimaryCaregiverFromGroupCommandHandler(
    private val authenticationServiceClient: IAuthenticationServiceClient
) : IRemovePrimaryCaregiverFromGroupCommandHandler {
    override suspend fun handleAsync(command: RemovePrimaryCaregiverFromGroupCommand) {
        // First handle the database transaction and get the user's keycloakId
        val keycloakId = withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

        // Then update the role in the authentication service
        executeWithExceptionLoggingAsync(
            operation = {
                authenticationServiceClient.updateRole(
                    id = keycloakId.toString(),
                    request = AuthUpdateRoleRequest(role = Role.FAMILY_MEMBER)
                )
            },
            logMessage = "Failed to update user role to FAMILY_MEMBER in authentication service"
        )
    }

    @Transactional
    fun handleTransaction(command: RemovePrimaryCaregiverFromGroupCommand): UUID {
        Log.debug("Attempting to remove primary caregiver from group ${command.groupId}")

        val group = GroupEntity.findById(command.groupId)
            ?: throw NotFoundException("Group not found").also {
                Log.error("Group not found with ID: ${command.groupId}")
            }

        if (group.primaryCaregiver == null) {
            throw BadRequestException("Group does not have a primary caregiver assigned")
        }

        val keycloakId = group.primaryCaregiver!!.keycloakId

        // Remove primary caregiver role but keep as family member
        group.removeAsPrimaryCaregiver(group.primaryCaregiver!!)
        group.persistAndFlush()

        Log.info("Successfully removed primary caregiver from group ${command.groupId}")
        return keycloakId!!
    }
}