package org.ptss.support.domain.commands.invitations

data class RegisterUserCommand(
    val firstName: String,
    val lastName: String,
    val password: String,
    val invitationCode: String
)