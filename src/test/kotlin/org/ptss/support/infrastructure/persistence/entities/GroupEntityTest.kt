package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.test.junit.QuarkusTest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ptss.support.domain.enums.Role
import org.ptss.support.infrastructure.persistence.config.BaseRepositoryTest
import java.time.OffsetDateTime
import java.util.UUID

@QuarkusTest
class GroupEntityTest : BaseRepositoryTest() {

    @Test
    @Transactional
    fun `should create valid group with all members`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HEALTHCARE_PROFESSIONAL)
        val primaryCaregiver = createUser(Role.FAMILY_MEMBER)

        // when
        val group = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
            this.primaryCaregiver = primaryCaregiver
        }
        entityManager.persist(group)
        flushAndClear()

        // then
        val savedGroup = GroupEntity.findById(group.id)
        assertNotNull(savedGroup)
        assertEquals(patient.id, savedGroup?.patient?.id)
        assertEquals(hcp.id, savedGroup?.healthcareProfessional?.id)
        assertEquals(primaryCaregiver.id, savedGroup?.primaryCaregiver?.id)
        assertTrue(savedGroup?.isActive ?: false)
    }

    @Test
    @Transactional
    fun `should create valid group without primary caregiver`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HEALTHCARE_PROFESSIONAL)

        // when
        val group = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
        }
        entityManager.persist(group)
        flushAndClear()

        // then
        val savedGroup = GroupEntity.findById(group.id)
        assertNotNull(savedGroup)
        assertNull(savedGroup?.primaryCaregiver)
    }

    @Test
    @Transactional
    fun `should fail when creating group without healthcare professional`() {
        // given
        val patient = createUser(Role.PATIENT)

        val group = GroupEntity().apply {
            this.patient = patient
        }

        // when/then
        assertThrows<jakarta.persistence.PersistenceException> {
            entityManager.persist(group)
            flushAndClear()
        }
    }

    @Test
    @Transactional
    fun `should prevent duplicate patient assignments`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp1 = createUser(Role.HEALTHCARE_PROFESSIONAL)
        val hcp2 = createUser(Role.HEALTHCARE_PROFESSIONAL)

        val group1 = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp1
        }
        entityManager.persist(group1)
        flushAndClear()

        // when/then
        val group2 = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp2
        }

        assertThrows<jakarta.persistence.PersistenceException> {
            entityManager.persist(group2)
            flushAndClear()
        }
    }

    @Test
    @Transactional
    fun `should add family members`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HEALTHCARE_PROFESSIONAL)
        val familyMember = createUser(Role.FAMILY_MEMBER)

        val group = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
        }
        entityManager.persist(group)

        // when
        val groupFamilyMember = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = familyMember
        }
        entityManager.persist(groupFamilyMember)
        flushAndClear()

        // then
        val savedGroup = GroupEntity.findById(group.id)
        assertEquals(1, savedGroup?.familyMembers?.size)
        assertEquals(familyMember.id, savedGroup?.familyMembers?.first()?.user?.id)
    }

    @Test
    @Transactional
    fun `should throw exception when promoting non-family member to primary caregiver`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HEALTHCARE_PROFESSIONAL)
        val nonFamilyMember = createUser(Role.FAMILY_MEMBER)

        val group = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
        }
        entityManager.persist(group)

        val groupFamilyMember = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = nonFamilyMember
        }

        // when/then
        assertThrows<IllegalArgumentException> {
            group.promoteToPrimaryCaregiver(groupFamilyMember)
        }
    }

    @Test
    @Transactional
    fun `should remove primary caregiver`() {
        // given
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HEALTHCARE_PROFESSIONAL)
        val caregiver = createUser(Role.FAMILY_MEMBER)

        val group = GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
            this.primaryCaregiver = caregiver
        }
        entityManager.persist(group)
        flushAndClear()

        // when
        val savedGroup = GroupEntity.findById(group.id)
        savedGroup?.removeAsPrimaryCaregiver(caregiver)
        flushAndClear()

        // then
        val updatedGroup = GroupEntity.findById(group.id)
        assertNull(updatedGroup?.primaryCaregiver)
    }

    private fun createUser(role: Role): UserEntity {
        return UserEntity().apply {
            keycloakId = UUID.randomUUID()
            firstName = "Test"
            lastName = "User"
            this.role = role
            lastSeen = OffsetDateTime.now()
        }.also {
            entityManager.persist(it)
            entityManager.flush()
        }
    }
}