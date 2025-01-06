package org.ptss.support.infrastructure.handlers.commands.users

import io.quarkus.logging.Log
import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.OptimisticLockException
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.users.DeleteUserCommand
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.commands.users.IDeleteUserCommandHandler
import org.ptss.support.infrastructure.persistence.entities.GroupEntity
import org.ptss.support.infrastructure.persistence.entities.GroupFamilyMemberEntity
import org.ptss.support.infrastructure.persistence.entities.UserEntity
import org.ptss.support.security.context.AuthenticatedUserContext

@ApplicationScoped
class DeleteUserCommandHandler(
    private val userContext: AuthenticatedUserContext
) : IDeleteUserCommandHandler {

    override suspend fun handleAsync(command: DeleteUserCommand) =
        withContext(Dispatchers.IO) {
            handleTransaction(command)
        }

    @Transactional
    @Throws(UnauthorizedException::class)
    fun handleTransaction(command: DeleteUserCommand) {
        Log.info("Processing delete user request for userId: ${command.userId}")
        val user = userContext.getCurrentUser()

        // Check self-deletion first
        if (user.userId == command.userId) {
            Log.warn("User ${user.userId} attempted to delete themselves")
            throw UnauthorizedException("Users cannot delete themselves")
        }

        val currentUserEntity = UserEntity.findById(user.userId)
            ?: run {
                Log.error("Current user ${user.userId} not found in database")
                throw UnauthorizedException("Current user not found")
            }
        val targetUser = UserEntity.findById(command.userId)
            ?: run {
                Log.warn("Target user ${command.userId} not found")
                throw NotFoundException("User not found")
            }

        // Permission check using entity helper
        if (!currentUserEntity.canDelete(targetUser)) {
            Log.warn("User ${user.userId} attempted to delete user ${targetUser.id} without permission")
            throw UnauthorizedException("No permission to delete this user")
        }

        Log.debug("Starting role-specific deletion process for user ${targetUser.id} with role ${targetUser.role}")
        // Role-specific deletion process
        try {
            when (targetUser.role) {
                Role.PATIENT -> deletePatient(targetUser)
                Role.PRIMARY_CAREGIVER -> deletePrimaryCaregiver(targetUser)
                Role.FAMILY_MEMBER -> deleteFamilyMember(targetUser)
                Role.HCP -> deleteHealthcareProfessional(targetUser)
                Role.ADMIN -> deleteAdmin(targetUser)
            }
        } catch (e: OptimisticLockException) {
            Log.warn("Deletion failed due to concurrent modification of user ${command.userId}")
            throw OptimisticLockException("User data was modified by another operation. Please try again.", e)
        }

        Log.info("Successfully deleted user ${targetUser.id}")
    }

    private fun deletePatient(patient: UserEntity) {
        Log.debug("Deleting patient ${patient.id}")
        GroupEntity.find("patient", patient).firstResult()
            ?: run {
                Log.error("Patient ${patient.id} has no associated group")
                throw IllegalStateException("Patient without group")
            }

        patient.delete() // Database cascades will handle group, group family member, and user deletion
        Log.info("Patient ${patient.id} and associated group deleted")
    }

    private fun deletePrimaryCaregiver(caregiver: UserEntity) {
        Log.debug("Deleting primary caregiver ${caregiver.id}")
        val membership = caregiver.groupFamilyMemberships.firstOrNull()
            ?: run {
                Log.error("Primary caregiver ${caregiver.id} has no group membership")
                throw IllegalStateException("Primary caregiver without group")
            }

        membership.group.removeAsPrimaryCaregiver(caregiver)
        Log.debug("Primary caregiver ${caregiver.id} demoted to family member")
        deleteFamilyMember(caregiver)
    }

    private fun deleteFamilyMember(familyMember: UserEntity) {
        Log.debug("Processing family member deletion ${familyMember.id}")
        val membership = familyMember.groupFamilyMemberships.firstOrNull()
            ?: run {
                Log.error("Family member ${familyMember.id} has no group membership")
                throw IllegalStateException("Family member without group")
            }

        GroupFamilyMemberEntity.anonymize(membership)
        Log.info("Family member ${familyMember.id} anonymized")
    }

    private fun deleteHealthcareProfessional(hcp: UserEntity) {
        Log.debug("Deleting healthcare professional ${hcp.id}")
        hcp.delete()
        Log.info("Healthcare professional ${hcp.id} deleted")
    }

    private fun deleteAdmin(admin: UserEntity) {
        Log.debug("Deleting admin ${admin.id}")
        admin.delete()
        Log.info("Admin ${admin.id} deleted")
    }
}