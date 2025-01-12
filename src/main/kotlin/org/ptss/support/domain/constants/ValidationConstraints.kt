package org.ptss.support.domain.constants

// Constants for Validation Constraints
object ValidationConstraints {
    // Length constraints
    const val NAME_MAX_LENGTH = 64
    const val EMAIL_MAX_LENGTH = 254
    const val VERIFICATION_CODE_LENGTH = 6
    const val RESET_CODE_LENGTH = 6

    // Pattern constraints
    const val NAME_PATTERN = "^[\\p{L}\\p{M}' .-]+$"
    const val VERIFICATION_CODE_PATTERN = "^[0-9]{$VERIFICATION_CODE_LENGTH}$"
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?])(?=\\S+$).{9,}$"
    const val PASSWORD_RESET_CODE_PATTERN = "^[0-9]{$VERIFICATION_CODE_LENGTH}$"
}