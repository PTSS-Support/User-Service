package org.ptss.support.domain.commands.forgot_password

data class ResetPasswordCommand(
    val resetCode: String,
    val newPassword: String
)