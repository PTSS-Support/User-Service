package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.test.junit.QuarkusTest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ptss.support.domain.constants.AnonymizationConstants.ANONYMIZED_VALUE
import org.ptss.support.domain.enums.Role
import org.ptss.support.infrastructure.persistence.config.BaseRepositoryTest
import java.time.OffsetDateTime
import java.util.UUID

@QuarkusTest
class GroupFamilyMemberEntityTest : BaseRepositoryTest() {

    @Test
    @Transactional
    fun `should create valid family member relationship`() {
        // given
        val group = createGroup()
        val familyMember = createUser(Role.FAMILY_MEMBER)

        // when
        val groupFamilyMember = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = familyMember
        }
        entityManager.persist(groupFamilyMember)
        flushAndClear()

        // then
        val saved = GroupFamilyMemberEntity.findById(groupFamilyMember.id)
        assertNotNull(saved)
        assertEquals(group.id, saved?.group?.id)
        assertEquals(familyMember.id, saved?.user?.id)
        assertNull(saved?.anonymizedAt)
    }

    @Test
    @Transactional
    fun `should prevent duplicate user assignments`() {
        // given
        val group = createGroup()
        val familyMember = createUser(Role.FAMILY_MEMBER)

        val firstAssignment = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = familyMember
        }
        entityManager.persist(firstAssignment)
        flushAndClear()

        // when/then
        val secondAssignment = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = familyMember
        }

        assertThrows<jakarta.persistence.PersistenceException> {
            entityManager.persist(secondAssignment)
            entityManager.flush()
        }
    }

    @Test
    @Transactional
    fun `should anonymize family member`() {
        // given
        val group = createGroup()
        val familyMember = createUser(Role.FAMILY_MEMBER)
        val groupFamilyMember = GroupFamilyMemberEntity().apply {
            this.group = group
            this.user = familyMember
        }
        entityManager.persist(groupFamilyMember)
        flushAndClear()

        // when
        val saved = GroupFamilyMemberEntity.findById(groupFamilyMember.id)!!
        GroupFamilyMemberEntity.anonymize(saved)
        flushAndClear()

        // then
        val anonymized = GroupFamilyMemberEntity.findById(groupFamilyMember.id)!!
        assertNotNull(anonymized.anonymizedAt)
        assertEquals(ANONYMIZED_VALUE, anonymized.user.firstName)
        assertEquals(ANONYMIZED_VALUE, anonymized.user.lastName)
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

    private fun createGroup(): GroupEntity {
        val patient = createUser(Role.PATIENT)
        val hcp = createUser(Role.HCP)

        return GroupEntity().apply {
            this.patient = patient
            this.healthcareProfessional = hcp
        }.also {
            entityManager.persist(it)
            entityManager.flush()
        }
    }
}