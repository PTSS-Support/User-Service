package org.ptss.support.domain.queries.invitations

data class GetInvitationByEmailAndCodeQuery(
    val email: String,
    val verificationCode: String
)