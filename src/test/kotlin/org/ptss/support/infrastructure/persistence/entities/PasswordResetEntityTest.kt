package org.ptss.support.infrastructure.persistence.entities

import io.quarkus.test.junit.QuarkusTest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.ptss.support.infrastructure.persistence.config.BaseRepositoryTest
import java.time.OffsetDateTime
import java.util.UUID

@QuarkusTest
class PasswordResetEntityTest : BaseRepositoryTest() {

    @Test
    @Transactional
    fun `should create valid password reset request`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "test@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
        }

        // when
        entityManager.persist(passwordReset)
        flushAndClear()

        // then
        val savedReset = PasswordResetEntity.findById(passwordReset.id)
        assertNotNull(savedReset)
        assertEquals(passwordReset.email, savedReset?.email)
        assertEquals(passwordReset.resetCode, savedReset?.resetCode)
        assertFalse(savedReset?.isVerified ?: true)
        assertFalse(savedReset?.isUsed ?: true)
    }

    @Test
    @Transactional
    fun `should find valid reset code`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "find@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
        }
        entityManager.persist(passwordReset)
        flushAndClear()

        // when
        val found = PasswordResetEntity.findValidResetCode(passwordReset.email, passwordReset.resetCode)

        // then
        assertNotNull(found)
        assertEquals(passwordReset.email, found?.email)
        assertEquals(passwordReset.resetCode, found?.resetCode)
    }

    @Test
    @Transactional
    fun `should not find expired reset code`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "expired@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().minusMinutes(1)
        }
        entityManager.persist(passwordReset)
        flushAndClear()

        // when
        val found = PasswordResetEntity.findValidResetCode(passwordReset.email, passwordReset.resetCode)

        // then
        assertNull(found)
    }

    @Test
    @Transactional
    fun `should not find used reset code`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "used@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
            isUsed = true
        }
        entityManager.persist(passwordReset)
        flushAndClear()

        // when
        val found = PasswordResetEntity.findValidResetCode(passwordReset.email, passwordReset.resetCode)

        // then
        assertNull(found)
    }

    @Test
    fun `should generate reset code with correct length`() {
        // given
        val passwordReset = PasswordResetEntity()

        // when
        val code = passwordReset.generateAndSetResetCode()

        // then
        assertEquals(6, code.length) // Assuming RESET_CODE_LENGTH is 6
        assertTrue(code.all { it.isDigit() })
    }

    @Test
    @Transactional
    fun `should set default expiration time if not initialized`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "default@example.com"
            generateAndSetResetCode()
        }

        // when
        entityManager.persist(passwordReset)
        flushAndClear()

        // then
        val saved = PasswordResetEntity.findById(passwordReset.id)
        assertNotNull(saved?.expiresAt)
        assertTrue(saved?.expiresAt?.isAfter(OffsetDateTime.now()) ?: false)
    }

    @Test
    @Transactional
    fun `should allow multiple reset requests for same email`() {
        // given
        val firstReset = PasswordResetEntity().apply {
            email = "multiple@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
        }
        val secondReset = PasswordResetEntity().apply {
            email = "multiple@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
        }

        // when
        entityManager.persist(firstReset)
        entityManager.persist(secondReset)
        flushAndClear()

        // then
        val savedFirst = PasswordResetEntity.findById(firstReset.id)
        val savedSecond = PasswordResetEntity.findById(secondReset.id)
        assertNotNull(savedFirst)
        assertNotNull(savedSecond)
        assertNotEquals(savedFirst?.resetCode, savedSecond?.resetCode)
    }

    @Test
    @Transactional
    fun `should handle verification status correctly`() {
        // given
        val passwordReset = PasswordResetEntity().apply {
            email = "verify@example.com"
            generateAndSetResetCode()
            expiresAt = OffsetDateTime.now().plusMinutes(30)
            isVerified = true
        }

        // when
        entityManager.persist(passwordReset)
        flushAndClear()

        // then
        val saved = PasswordResetEntity.findById(passwordReset.id)
        assertTrue(saved?.isVerified ?: false)
    }
}