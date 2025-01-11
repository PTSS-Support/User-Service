package org.ptss.support.domain.commands.forgot_password

data class RequestPasswordResetCommand(
    val email: String
)