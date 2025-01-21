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
class InvitationEntityTest : BaseRepositoryTest() {

    @Test
    @Transactional
    fun `should create valid invitation for PATIENT role`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "test@example.com"
            role = Role.PATIENT
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
        }

        // when
        entityManager.persist(invitation)
        flushAndClear()

        // then
        val savedInvitation = InvitationEntity.findById(invitation.id)
        assertNotNull(savedInvitation)
        assertEquals(invitation.email, savedInvitation?.email)
        assertEquals(invitation.role, savedInvitation?.role)
        assertEquals(invitation.groupId, savedInvitation?.groupId)
        assertEquals(invitation.verificationCode, savedInvitation?.verificationCode)
        assertFalse(savedInvitation?.isVerified ?: true)
        assertFalse(savedInvitation?.isRegistered ?: true)
    }

    @Test
    @Transactional
    fun `should create valid invitation for FAMILY_MEMBER role`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "family@example.com"
            role = Role.FAMILY_MEMBER
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
        }

        // when
        entityManager.persist(invitation)
        flushAndClear()

        // then
        val savedInvitation = InvitationEntity.findById(invitation.id)
        assertNotNull(savedInvitation)
        assertEquals(Role.FAMILY_MEMBER, savedInvitation?.role)
    }

    @Test
    @Transactional
    fun `should throw exception when creating invitation with ADMIN role`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "admin@example.com"
            role = Role.ADMIN
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
        }

        // when/then
        assertThrows<IllegalArgumentException> {
            entityManager.persist(invitation)
            flushAndClear()
        }
    }

    @Test
    @Transactional
    fun `should find invitation by email and verification code`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "find@example.com"
            role = Role.PATIENT
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
        }
        entityManager.persist(invitation)
        flushAndClear()

        // when
        val found = InvitationEntity.findByEmailAndVerificationCode(invitation.email, invitation.verificationCode)

        // then
        assertNotNull(found)
        assertEquals(invitation.email, found?.email)
        assertEquals(invitation.verificationCode, found?.verificationCode)
    }

    @Test
    @Transactional
    fun `should not find expired invitation`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "expired@example.com"
            role = Role.PATIENT
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().minusHours(1)
        }
        entityManager.persist(invitation)
        flushAndClear()

        // when
        val found = InvitationEntity.findByEmailAndVerificationCode(invitation.email, invitation.verificationCode)

        // then
        assertNull(found)
    }

    @Test
    @Transactional
    fun `should not find verified invitation`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "verified@example.com"
            role = Role.PATIENT
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
            isVerified = true
        }
        entityManager.persist(invitation)
        flushAndClear()

        // when
        val found = InvitationEntity.findByEmailAndVerificationCode(invitation.email, invitation.verificationCode)

        // then
        assertNull(found)
    }

    @Test
    fun `should generate verification code with correct length`() {
        // given
        val invitation = InvitationEntity()

        // when
        val code = invitation.generateAndSetVerificationCode()

        // then
        assertEquals(6, code.length) // Assuming VERIFICATION_CODE_LENGTH is 6
        assertTrue(code.all { it.isDigit() })
    }

    @Test
    @Transactional
    fun `should throw exception when role is not initialized`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "test@example.com"
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
            expiresAt = OffsetDateTime.now().plusHours(24)
        }

        // when/then
        assertThrows<IllegalStateException> {
            entityManager.persist(invitation)
            flushAndClear()
        }
    }

    @Test
    @Transactional
    fun `should set default expiration time if not initialized`() {
        // given
        val invitation = InvitationEntity().apply {
            email = "default@example.com"
            role = Role.PATIENT
            groupId = UUID.randomUUID()
            generateAndSetVerificationCode()
        }

        // when
        entityManager.persist(invitation)
        flushAndClear()

        // then
        val saved = InvitationEntity.findById(invitation.id)
        assertNotNull(saved?.expiresAt)
        assertTrue(saved?.expiresAt?.isAfter(OffsetDateTime.now()) ?: false)
    }
}