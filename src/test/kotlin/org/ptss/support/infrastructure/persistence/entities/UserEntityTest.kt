package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.ptss.support.domain.enums.Role
import org.ptss.support.infrastructure.persistence.config.BaseRepositoryTest
import java.time.OffsetDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import jakarta.transaction.Transactional

@QuarkusTest
@Transactional
class UserEntityTest : BaseRepositoryTest() {

    private lateinit var admin: UserEntity
    private lateinit var hcp: UserEntity
    private lateinit var patient: UserEntity
    private lateinit var primaryCaregiver: UserEntity
    private lateinit var familyMember: UserEntity
    private lateinit var group: GroupEntity

    @BeforeEach
    @Transactional
    fun setup() {
        // Create test users
        admin = createUser(Role.ADMIN)
        hcp = createUser(Role.HCP)
        patient = createUser(Role.PATIENT)
        primaryCaregiver = createUser(Role.PRIMARY_CAREGIVER)
        familyMember = createUser(Role.FAMILY_MEMBER)

        // Create group structure
        group = GroupEntity().apply {
            patient = this@UserEntityTest.patient
            healthcareProfessional = hcp
            primaryCaregiver = this@UserEntityTest.primaryCaregiver
        }
        group.persistAndFlush()

        // Create family member relationship
        GroupFamilyMemberEntity().apply {
            user = familyMember
            group = this@UserEntityTest.group
        }.persistAndFlush()

        flushAndClear()
    }

    private fun createUser(role: Role): UserEntity {
        return UserEntity().apply {
            keycloakId = UUID.randomUUID()
            firstName = "Test"
            lastName = "User"
            lastSeen = OffsetDateTime.now()
            this.role = role
        }.also { it.persistAndFlush() }
    }

    @Nested
    inner class CanDeleteTests {
        @Test
        fun `admin can delete any user except self`() {
            assertTrue(admin.canDelete(hcp))
            assertTrue(admin.canDelete(patient))
            assertTrue(admin.canDelete(primaryCaregiver))
            assertTrue(admin.canDelete(familyMember))
            assertFalse(admin.canDelete(admin))
        }

        @Test
        fun `HCP can only delete their patients`() {
            assertTrue(hcp.canDelete(patient))
            assertFalse(hcp.canDelete(admin))
            assertFalse(hcp.canDelete(hcp))
            assertFalse(hcp.canDelete(primaryCaregiver))
            assertFalse(hcp.canDelete(familyMember))

            // Create another patient not associated with this HCP
            val otherPatient = createUser(Role.PATIENT)
            assertFalse(hcp.canDelete(otherPatient))
        }

        @Test
        fun `patient can delete their family members and primary caregiver`() {
            assertTrue(patient.canDelete(primaryCaregiver))
            assertTrue(patient.canDelete(familyMember))
            assertFalse(patient.canDelete(admin))
            assertFalse(patient.canDelete(hcp))
            assertFalse(patient.canDelete(patient))
        }

        @Test
        fun `primary caregiver can only delete family members in their group`() {
            assertTrue(primaryCaregiver.canDelete(familyMember))
            assertFalse(primaryCaregiver.canDelete(admin))
            assertFalse(primaryCaregiver.canDelete(hcp))
            assertFalse(primaryCaregiver.canDelete(patient))
            assertFalse(primaryCaregiver.canDelete(primaryCaregiver))

            // Create another family member not in this group
            val otherFamilyMember = createUser(Role.FAMILY_MEMBER)
            assertFalse(primaryCaregiver.canDelete(otherFamilyMember))
        }

        @Test
        fun `family member cannot delete anyone`() {
            assertFalse(familyMember.canDelete(admin))
            assertFalse(familyMember.canDelete(hcp))
            assertFalse(familyMember.canDelete(patient))
            assertFalse(familyMember.canDelete(primaryCaregiver))
            assertFalse(familyMember.canDelete(familyMember))
        }
    }

    @Nested
    inner class ValidationTests {
        @Test
        fun `cannot delete last admin`() {
            assertThrows<IllegalStateException> {
                admin.validateDeletion()
            }
        }

        @Test
        @Transactional
        fun `can delete admin when multiple exist`() {
            // Create another admin
            createUser(Role.ADMIN)
            assertDoesNotThrow {
                admin.validateDeletion()
            }
        }

        @Test
        @Transactional
        fun `cannot delete HCP with active groups`() {
            assertThrows<IllegalStateException> {
                hcp.validateDeletion()
            }
        }

        @Test
        fun `can delete HCP without active groups`() {
            // Remove HCP from group
            group.delete()
            flushAndClear()

            assertDoesNotThrow {
                hcp.validateDeletion()
            }
        }

        @Test
        @Transactional
        fun `patient deletion validation requires group`() {
            // Create a patient without group
            val lonePatient = createUser(Role.PATIENT)

            assertThrows<IllegalStateException> {
                lonePatient.validateDeletion()
            }

            // Patient with group should pass validation
            assertDoesNotThrow {
                patient.validateDeletion()
            }
        }

        @Test
        fun `family members can only be deleted through patient cascade`() {
            assertThrows<IllegalStateException> {
                familyMember.validateDeletion()
            }

            // Simulate patient deletion cascade
            patient.delete()
            flushAndClear()

            assertDoesNotThrow {
                familyMember.validateDeletion()
            }
        }
    }
}