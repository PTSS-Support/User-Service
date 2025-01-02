package org.ptss.support.domain.commands.invitations

data class VerifyInvitationCommand(
    val email: String,
    val verificationCode: String
)