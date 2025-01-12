package org.ptss.support.domain.commands.forgot_password

data class VerifyPasswordResetCommand(
    val email: String,
    val resetCode: String
)